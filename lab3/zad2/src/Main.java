import java.util.Random;

public class Main {
    static int iloscZwierzat = 10;
    static int iloscGospodarstw = 5;
    static Gospodarstwo gosp1;

    public static void main(String[] args) {
        gosp1 = new Gospodarstwo(iloscGospodarstw, iloscZwierzat);
        for (int i = 0; i < iloscZwierzat; i++)
            new Zwierze(i, 2000, gosp1).start();
    }
}
class Zwierze extends Thread {
    static int PORT = 1;
    static int START = 2;
    static int PRACA = 3;
    static int POWROT = 4;
    static int KATASTROFA = 5;
    static int JEDZ = 1000;
    static int RESZTKI = 500;
    int numer;
    int jedzenie;
    int stan;
    Gospodarstwo g;
    Random rand;

    public Zwierze(int numer, int jedzenie, Gospodarstwo g) {
        this.numer = numer;
        this.jedzenie = jedzenie;
        this.stan = PRACA;
        this.g = g;
        rand = new Random();
    }

    public void run() {
        while (true) {
            if (stan == PORT) {
                if (rand.nextInt(2) == 1) {
                    stan = START;
                    jedzenie = JEDZ;
                    System.out.println("Cekamy na wypuszczenie zwierzecia " + numer);
                    stan = g.wyjdzNaPole(numer);
                } else {
                    System.out.println("Postoje sobie jeszcze troche");
                }
            } else if (stan == START) {
                System.out.println("Zwierze wyszło " + numer);
                stan = PRACA;
            } else if (stan == PRACA) {
                jedzenie -= rand.nextInt(500);
                System.out.println("Zwierze " + numer + " w drodze");
                if (jedzenie <= RESZTKI) {
                    stan = POWROT;
                } else try {
                    sleep(rand.nextInt(1000));
                } catch (Exception e) {
                }
            } else if (stan == POWROT) {
                System.out.println("Prosze o pozowolenie na wpuszczenie do chlewa " + numer + " ilosc jedzenia " + jedzenie);
                stan = g.wejdzNaPole();
                if (stan == POWROT) {
                    jedzenie -= rand.nextInt(500);
                    System.out.println("RESZTKI " + jedzenie);
                    if (jedzenie <= 0) stan = KATASTROFA;
                }
            } else if (stan == KATASTROFA) {
                System.out.println("KATASTROFA Zwierzecia " + numer);
                g.zrobJedzenie();
            }
        }
    }
}

class Gospodarstwo {
    static int Gospodarstwo = 1;
    static int START = 2;
    static int PRACA = 3;
    static int POWROT = 4;
    static int KATASTROFA = 5;
    int iloscGospodarstw;
    int iloscZwierzat;
    int iloscZajetych;;

    Gospodarstwo(int iloscGospodarstw, int iloscZwierzat) {
        this.iloscGospodarstw = iloscGospodarstw;
        this.iloscZwierzat = iloscZwierzat;
        this.iloscZajetych = 0;
    }

    synchronized int wyjdzNaPole(int numer) {
        iloscZajetych--;
        System.out.println("Pozwolenie na wyjscie na pole " + numer);
        return START;
    }

    synchronized int wejdzNaPole() {
        try {
            Thread.currentThread().sleep(1000);
        } catch (Exception ie) {
        }
        if (iloscZajetych < iloscGospodarstw) {
            iloscZajetych++;
            System.out.println("Pozwolenie na wpuszczenie zwierzecia " + iloscZajetych);
            return Gospodarstwo;
        } else {
            return POWROT;
        }
    }

    synchronized void zrobJedzenie() {
        iloscZwierzat--;
        System.out.println("Przerobiona na jedzenie");
        if (iloscZwierzat == iloscGospodarstw) System.out.println("Ilosc Zwierzat taka sama jak gospodarstw");
    }
}