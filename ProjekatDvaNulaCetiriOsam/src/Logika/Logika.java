package Logika;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Logika {
	//Privatni atributi sem Highscore
    int[][] tabelaZaIgru; //Tabela stanja
    int tabelaVisina;//Visina tabele 5 ili 4
    int tabelaSirina;//Sirina tabele 5 ili 4
    boolean kraj = false;//Kraj igre
    int Rezultat = 0;//Trenutni rezultat
    int Dimenzija;//Int za odabir dimenzije 4x4 ili 5x5
    public int HighScore = 0;//Najveci rezultat. Upisuje ga jedino ako je veči od največeg
    private final String PamtiHighScore = "src/data/highscore.txt"; // Fajl za spašavanje Highscore
    
    public Logika(int dimenzija) {
    	//Odabir dimenzije i postavljanje private atributa
        Dimenzija = dimenzija;
        if (Dimenzija == 5) {
            tabelaSirina = 5;
            tabelaVisina = 5;
        } else if (Dimenzija == 4) {
            tabelaSirina = 4;
            tabelaVisina = 4;
        }
        tabelaZaIgru = new int[tabelaSirina][tabelaVisina];
        // Inicijalizacija tabele na nule
        for (int i = 0; i < tabelaSirina; i++) {
            for (int j = 0; j < tabelaVisina; j++) {
                tabelaZaIgru[i][j] = 0;
            }
        }

        UcitajHighScore();
        UcitajStanjeIgre();

        // Proverite da li je tabela prazna; ako jeste, dodajte jedan broj(2/4)
        boolean prazna = true;
        for (int i = 0; i < tabelaSirina; i++) {
            for (int j = 0; j < tabelaVisina; j++) {
                if (tabelaZaIgru[i][j] != 0) {
                    prazna = false;
                    break;
                }
            }
        }
        //Ako je prazno polje dodaj broj(da se de bi desilo da prepisuje vec psotojeci)
        if (prazna) {
            dodajBroj();
        }
    }


    //Inspektorske metode. Samo vračaju vrijednost. Koriste se u GUI i Konzoli
    public boolean getKraj() {
    	return kraj;
    }
    public int getRezultat() {
    	return Rezultat;
    }
    public int getVisina() {
    	return tabelaVisina;
    }
    
    public int getSirina() {
    	return tabelaSirina;
    }
    
    public int[][] getTabela(){
    	return tabelaZaIgru;
    }
    
    //Funkcije koje vracaju slucajan broj x i y. Pozicije novih figura na tabeli
    public int RandomX() {
		return (int) Math.floor(Math.random()*tabelaVisina);
    }
    public int RandomY() {
    	return (int) Math.floor(Math.random()*tabelaSirina);
    }
    //Metoda za stavranje nasumicnih brojeva. Generisanje random broja od 0 do 1, ako je manji od 1/2 onda je 2.
    public int RandomFigura() {
    	if(Math.random() < 0.5) {
    		return 2;}
    	return 4;	
    }
    
    //metoda koja vraca kopiju tabele(stanja) igre, korisiti se da bi se provjerilo da ako su svi brojevi vec dole
    //i pomjerimo ih opet dole, da se ne dodaje novi broj
    private int[][] KopijaTabele(int [][] T){
    	int[][] kopija = new int[T.length][T[0].length];
        for (int i = 0; i < T.length; i++) {
            kopija[i] = Arrays.copyOf(T[i], T[i].length);
        }
        return kopija;
    }
    
    //Metoda koja provjerava je li isto stanje trenutne tabele i kopije tabele prije pomjeranja
    private boolean JeLiIstoStanje(int[][] a, int[][] b) {
    	for (int i = 0; i < a.length; i++) {
            if (!Arrays.equals(a[i], b[i])) {
                return false;
            }
        }
        return true;
    }
    
    //Metoda koja dodaje broj
    private void dodajBroj() {
        boolean zauzeto = true;
        while (zauzeto) { //Sve dok je NIJE zauzeto 
            int x = RandomX();
            int y = RandomY();
            if (tabelaZaIgru[x][y] == 0) {//Ako je polje prazno dodaj
                tabelaZaIgru[x][y] = RandomFigura();
                zauzeto = false;//Nije zauzeto = false
            }
            SpasiStanjeIgre();//Nakon svakog dodavanja broja spasava se stanje Tabele
            if (ProvjeriPoraz()) { //Nakon svakog dodavanja novog broja privjerava se Kraj igre
            	kraj = true;
                break;
            }
        }
    }
    
    //Metoda za pomjeranje prema dole
    public void PomjeriDole() {
    	int[][] pocetnoStanje = KopijaTabele(tabelaZaIgru);//Pravi početno stanje tabele.
    	//Za svaki red
        for (int col = 0; col < tabelaSirina; col++) {
            int[] temp = new int[tabelaVisina];
            int index = tabelaVisina - 1;
            //Pomjeramo sve figure na tu stranu(prema dole)
            for (int red = tabelaVisina - 1; red >= 0; red--) {
                if (tabelaZaIgru[red][col] != 0) {
                    temp[index] = tabelaZaIgru[red][col];
                    index--;
                }
            }
            //Ako se dva ista sudare onda da se spoje u duplo vecu figuru
            for (int red = tabelaVisina - 1; red > 0; red--) {
                if (temp[red] == temp[red - 1] && temp[red] != 0) {
                    temp[red] *= 2; 
                    Rezultat += temp[red];
                    temp[red - 1] = 0; 
                }
            }
            
            //Spojene koje su viška obriši ih(postavi na 0)
            index = tabelaVisina - 1; 
            for (int red = tabelaVisina - 1; red >= 0; red--) {
                if (temp[red] != 0) {
                    tabelaZaIgru[index][col] = temp[red];
                    index--;
                }
            }
            //Figure koja je spojena nije obrisana, Ovdje brisemo zadnju u redu koja je spojena
            for (int red = index; red >= 0; red--) {
                tabelaZaIgru[red][col] = 0;
            }
        }
        //Ako se satanje promjenilo dodaj broj
        if (!JeLiIstoStanje(pocetnoStanje, tabelaZaIgru)) {
            dodajBroj();
        }
        
        //Ako je doslo do broja 2048 zavrsi igru
        if (ProvjeriPobjedu()) {
            kraj = true;
        }
    }
    
    //Metoda pomjeri prema gore
    public void PomjeriGore() {
    	int[][] pocetnoStanje = KopijaTabele(tabelaZaIgru);
    	//Za svaki red
        for (int col = 0; col < tabelaSirina; col++) {
            int[] temp = new int[tabelaVisina];
            int index = 0;
            //Provjeravamo odozdo prema gore, pomjreamo figuru ako je slobodno mjesto
            for (int red = 0; red < tabelaVisina; red++) {
                if (tabelaZaIgru[red][col] != 0) {
                    temp[index] = tabelaZaIgru[red][col];
                    index++;
                }
            }
            //Ako su iste iduci odozdo prema gore, spoji ih
            for (int red = 0; red < tabelaVisina - 1; red++) {
                if (temp[red] == temp[red + 1] && temp[red] != 0) {
                    temp[red] *= 2;
                    Rezultat += temp[red];
                    temp[red + 1] = 0; 
                }
            }


            index = 0; 
            //Spojene koje su viška obriši ih(postavi na 0)
            for (int red = 0; red < tabelaVisina; red++) {
                if (temp[red] != 0) {
                    tabelaZaIgru[index][col] = temp[red];
                    index++;
                }
            }

            for (int red = index; red < tabelaVisina; red++) {
                tabelaZaIgru[red][col] = 0;
            }
        }
      //Ako se satanje promjenilo dodaj broj
        if (!JeLiIstoStanje(pocetnoStanje, tabelaZaIgru)) {
            dodajBroj(); 
        }
      //Ako je doslo do broja 2048 zavrsi igru
        if (ProvjeriPobjedu()) {
            kraj = true;
        }
    }
    
    //Metoda za pomjeranje desno
    public void PomjeriDesno() {
    	int[][] pocetnoStanje = KopijaTabele(tabelaZaIgru);
    	//Za svaku kolonu
        for (int row = 0; row < tabelaVisina; row++) {
            int[] temp = new int[tabelaSirina];
            int index = tabelaSirina - 1;
            //Iduci od kraja prema pocetku reda, pomjrei prema desno ako je prazno
            for (int col = tabelaSirina - 1; col >= 0; col--) {
                if (tabelaZaIgru[row][col] != 0) {
                    temp[index] = tabelaZaIgru[row][col];
                    index--;
                }
            }
          //Ako su iste iduci s desna na lijevo, spoji ih
            for (int col = tabelaSirina - 1; col > 0; col--) {
                if (temp[col] == temp[col - 1] && temp[col] != 0) {
                    temp[col] *= 2;
                    Rezultat += temp[row];
                    temp[col - 1] = 0;
                }
            }
          //Spojene koje su viška obriši ih(postavi na 0)
            index = tabelaSirina - 1;
            for (int col = tabelaSirina - 1; col >= 0; col--) {
                if (temp[col] != 0) {
                    tabelaZaIgru[row][index] = temp[col];
                    index--;
                }
            }

            for (int col = index; col >= 0; col--) {
                tabelaZaIgru[row][col] = 0;
            }
        }
      //Ako se satanje promjenilo dodaj broj
        if (!JeLiIstoStanje(pocetnoStanje, tabelaZaIgru)) {
            dodajBroj(); // Add a new block only if the state has changed
        }
      //Ako je doslo do broja 2048 zavrsi igru
        if (ProvjeriPobjedu()) {
            kraj = true;
        }
    }
    //Metoda za pomjeranje lijevo
    public void PomjeriLijevo() {
    	int[][] pocetnoStanje = KopijaTabele(tabelaZaIgru);
    	//Za svaku red pocevsi od pocetka
        for (int row = 0; row < tabelaSirina; row++) {
            int[] temp = new int[tabelaSirina];
            int index = 0; 
          //Iduci od kraja prema pocetku reda, pomjrei prema lijevo ako je prazno
            for (int col = 0; col < tabelaVisina; col++) {
                if (tabelaZaIgru[row][col] != 0) {
                    temp[index] = tabelaZaIgru[row][col];
                    index++;
                }
            }
          //Ako su iste iduci s lijeva na desno, spoji ih
            for (int col = 0; col < tabelaSirina - 1; col++) {
                if (temp[col] == temp[col + 1] && temp[col] != 0) {
                    temp[col] *= 2;
                    Rezultat += temp[row];
                    temp[col + 1] = 0;
                }
            }
          //Spojene koje su viška obriši ih(postavi na 0)
            index = 0; 
            for (int col = 0; col < tabelaSirina; col++) {
                if (temp[col] != 0) {
                    tabelaZaIgru[row][index] = temp[col];
                    index++;
                }
            }

            for (int col = index; col < tabelaSirina; col++) {
                tabelaZaIgru[row][col] = 0;
            }
        }
      //Ako se satanje promjenilo dodaj broj
        if (!JeLiIstoStanje(pocetnoStanje, tabelaZaIgru)) {
            dodajBroj();
        }
      //Ako je doslo do broja 2048 zavrsi igru
        if (ProvjeriPobjedu()) {
            kraj = true;
        }
    }

    
    //Funkcija koja cita Highscore s highscore.txt
    private void UcitajHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PamtiHighScore))) {
            HighScore = Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            HighScore = 0; // Default high score if file doesn't exist or is corrupted
        }
    }
    
    //Metoda za spasavanje Highscore u highscore.txt
    private void SpasiHighScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PamtiHighScore))) {
            writer.write(String.valueOf(HighScore));
        } catch (IOException e) {
            System.err.println("Error saving high score: " + e.getMessage());
        }
    }
    
    //Metoda koja updatuje highscore ako je trenutni veci od već postojučeg
    private void UpdatujHighScore() {
        if (Rezultat > HighScore) {
            HighScore = Rezultat;
            SpasiHighScore();
        }
    }
    
    //Metoda za provjeru pobjede odnosno ako se nadze broj 2048
    public boolean ProvjeriPobjedu() {
        for (int i = 0; i < tabelaSirina; i++) {
            for (int j = 0; j < tabelaVisina; j++) {
                if (tabelaZaIgru[i][j] == 2048) {
                	ResetujIgru();
                    return true;
                }
            }
        }
        return false;
    }
    
    //Metoda za provjeravanje poraza, tj. Ako je ploca ispunjena i nije moguce uraditi nijedan više potez
    private boolean ProvjeriPoraz() {
        // Provjerava ima li praznih mjesta
        for (int i = 0; i < tabelaSirina; i++) {
            for (int j = 0; j < tabelaVisina; j++) {
                if (tabelaZaIgru[i][j] == 0) {
                    return false; // Nađeno prazno polje, returnaj false(nije kraj)
                }
            }
        }

        // Provjerava da li ima mogucih koji se mogu spojit horizontalno
        for (int i = 0; i < tabelaVisina; i++) {
            for (int j = 0; j < tabelaSirina - 1; j++) {
                if (tabelaZaIgru[i][j] == tabelaZaIgru[i][j + 1]) {
                    return false; // Spajanje moguče, nije kraj
                }
            }
        }

     // Provjerava da li ima mogucih koji se mogu spojit vertikalno
        for (int j = 0; j < tabelaSirina; j++) {
            for (int i = 0; i < tabelaVisina - 1; i++) {
                if (tabelaZaIgru[i][j] == tabelaZaIgru[i + 1][j]) {
                    return false; // Spajanje moguče, nije kraj
                }
            }
        }

        // Ako nema praznih i nije moguče spajanje resetuj stanje i igru i updatuje Highscore ako je moguće
        ResetujIgru();
        UpdatujHighScore();
        return true;
    }
    

	 // Metoda za spašavanje stanja igre
    private String generirajNazivDatoteke() {
        return "src/data/game_state_" + Dimenzija + "x" + Dimenzija + ".txt"; // Dinamički naziv datoteke
    }

    // Spremanje stanja igre
    public void SpasiStanjeIgre() {
        String nazivDatoteke = generirajNazivDatoteke();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nazivDatoteke))) {
            writer.write(Dimenzija + "\n"); // Spremanje dimenzije
            writer.write(Rezultat + "\n");  // Spremanje trenutnog rezultata
            for (int i = 0; i < tabelaSirina; i++) {
                for (int j = 0; j < tabelaVisina; j++) {
                    writer.write(tabelaZaIgru[i][j] + " "); // Spremanje vrijednosti tabele
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Greška prilikom spremanja stanja igre: " + e.getMessage());
        }
    }

    // Učitavanje stanja igre
    public void UcitajStanjeIgre() {
        String nazivDatoteke = generirajNazivDatoteke();
        try (BufferedReader reader = new BufferedReader(new FileReader(nazivDatoteke))) {
            Dimenzija = Integer.parseInt(reader.readLine()); // Učitavanje dimenzije
            Rezultat = Integer.parseInt(reader.readLine());  // Učitavanje rezultata
            tabelaSirina = Dimenzija;
            tabelaVisina = Dimenzija;
            tabelaZaIgru = new int[tabelaSirina][tabelaVisina];
            for (int i = 0; i < tabelaSirina; i++) {
                String[] values = reader.readLine().split(" "); // Učitavanje reda tabele
                for (int j = 0; j < tabelaVisina; j++) {
                    tabelaZaIgru[i][j] = Integer.parseInt(values[j]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Nema spremljenog stanja za " + Dimenzija + "x" + Dimenzija + ". Počinje nova igra.");
            Rezultat = 0;
            tabelaZaIgru = new int[tabelaSirina][tabelaVisina];
        }
    }

    
    public void ResetujIgru() {
        Rezultat = 0;
        tabelaZaIgru = new int[tabelaSirina][tabelaVisina]; // Resetovanje tabele na sve nule
        kraj = true; // Igra završena
        dodajBroj(); // Dodajemo prvi broj na početak igre
        
    }
 





 
}

