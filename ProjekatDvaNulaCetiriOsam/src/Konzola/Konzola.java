package Konzola;

import Logika.Logika;
import java.util.Scanner;

public class Konzola {
    Logika Igra = new Logika(5);

    /**
     * Konstruktor klase Konzola. Inicijalizuje igru, učitava prethodno spremljeno stanje,
     * i pokreće petlju igre dok se igra ne završi.
     */
    public Konzola() {
        System.out.println("Najbolji rezultat: " + Igra.HighScore);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Unesite dimenziju tabele(4x4 | 5x5): ");
        char dim = scanner.next().charAt(0);
        int dimenzija = Character.getNumericValue(dim);
        Igra = new Logika(dimenzija);
        System.out.println("Pokušavam učitati prethodno spremljeno stanje igre...");
        Igra.UcitajStanjeIgre();
        System.out.println("Stanje igre je učitano.");
        while (!Igra.getKraj()) {
            IspisiStanje(); // Prikazuje trenutno stanje tabele
            Potez(); // Omogućava korisniku unos poteza
        }
        IspisiStanje();
        System.out.println("Rezultat je: " + Igra.getRezultat());

        if (Igra.getKraj()) {
            System.out.println("Igra je završena. Spremanje stanja igre...");
            Igra.SpasiStanjeIgre();
            System.out.println("Stanje igre je spremljeno.");
        }
    }
    /**
     * Omogućava unos poteza od strane korisnika i izvršava odgovarajuću akciju.
     * Korisnik unosi jedan od sledećih poteza:
     * 'u' - pomeranje gore,
     * 'd' - pomeranje dole,
     * 'l' - pomeranje levo,
     * 'r' - pomeranje desno.
     * data izbacit iz src. preimoenovat medode na mala slova varijable na velka package na mala
     * generisat api dokumentaciju. U dijagramu izbirsat gui i konzolu, a razgradit napravi potez
     */
    /**
     * 
     */
    public void Potez() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Unesite potez ('u' za gore, 'd' za dole, 'l' za lijevo, 'r' za desno): ");
        char smjer = scanner.next().charAt(0);
        switch (smjer) {
            case 'u':
                Igra.PomjeriGore();
                break;
            case 'd':
                Igra.PomjeriDole();
                break;
            case 'l':
                Igra.PomjeriLijevo();
                break;
            case 'r':
                Igra.PomjeriDesno();
                break;
            default:
                System.out.println("Nepoznat potez! Koristite 'u', 'd', 'l', 'r'.");
        }
    }
    /**
     * Ispisuje trenutno stanje tabele igre na konzoli.
     * Formatira izlaz tako da svaka vrednost bude prikazana u koloni širine 5 karaktera.
     */
    public void IspisiStanje() {
        System.out.println("_____________"+ Igra.getRezultat() + "__________________");
        for (int i = 0; i < Igra.getVisina(); i++) {
            for (int j = 0; j < Igra.getSirina(); j++) {
                System.out.printf("%5d", Igra.getTabela()[i][j]); // Ispisuje vrednost trenutnog polja tabele
            }
            System.out.println();
            System.out.println(); // Dodaje razmak između redova za bolju preglednost
        }
    }
}
