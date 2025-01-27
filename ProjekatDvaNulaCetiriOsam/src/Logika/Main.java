package Logika;

import Grafika.GUIVerzija;
import Konzola.Konzola;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //Meni za odabir verzije igre
        System.out.println("Izaberite vrstu igre:");
        System.out.println("1 - GUI verzija");
        System.out.println("2 - Konzolna verzija");
        System.out.print("Vaš izbor: ");
        //Odabir Konzola/GUI
        int izbor = scanner.nextInt();
        
        if (izbor == 1) {//GUI
            System.out.println("Pokreće se GUI verzija...");
            GUIVerzija gui = new GUIVerzija();
        } else if (izbor == 2) {//Konzola
            System.out.println("Pokreće se konzolna verzija...");
            Konzola k = new Konzola();
        } else {//Ostalo
            System.out.println("Nevažeći unos. Molimo pokušajte ponovo.");
        }

        scanner.close();
    }
}
