package Grafika;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Logika.Logika;

public class GUIVerzija extends JFrame {
    private JButton[][] buttons;
    private Logika logika = new Logika(4);
    private JLabel rezultatLabel; // Atribut za prikaz rezultata
    
  //Inicijalizacija
    public GUIVerzija() {
    	MyWindow();
    }
    
    //Inicijalziacija menija
    public void MyWindow() {
        setTitle("2048");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500); 
        setLocationRelativeTo(null);
        
        
        displayMenu();
    }
    
    //Pravljenje menija sa 2 dugmeta 4x4 i 5x5 i prikazivanje Higshcore
    private void displayMenu() {
    	//Layout postavljamo na grid
    	GridBagConstraints c = new GridBagConstraints();
    	setLayout(new GridBagLayout());
    	setBackground(Color.DARK_GRAY);
    	
    	//Atributi koje koristimo
		JLabel naslov;
		JLabel byFarisLindov;
		JLabel highScoreLabel;
		JButton cetiri;
		JButton pet;
		
		//Postavljanje atributa
		naslov     = new JLabel("2048");
		byFarisLindov     = new JLabel("By Faris Lindov");
		cetiri     = new JButton("4x4");
		pet        = new JButton("5x5");
		
		//Uređivanje i stavljanje u grid
		naslov.setFont(new Font("Monospaced", Font.BOLD, 42));
		naslov.setForeground(Color.ORANGE);
		c.anchor = GridBagConstraints.CENTER;
		c.gridx=0;
		c.gridy=0;
		c.gridwidth = 2; 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(20,4,190,4); 
		add(naslov, c);
		
		byFarisLindov.setFont(new Font("Monospaced", Font.PLAIN, 24));
		byFarisLindov.setForeground(Color.ORANGE);
		c.anchor = GridBagConstraints.CENTER;
		c.gridx=0;
		c.gridy=0;
		c.gridwidth = 2; 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(40,4,130,4); 
		add(byFarisLindov, c);
		
		int highScore = logika.HighScore;
		highScoreLabel = new JLabel();
	    highScoreLabel.setText("Najveci rezultat: " + highScore);
	    highScoreLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));
	    highScoreLabel.setForeground(Color.ORANGE);
	    c.gridy = 2;
	    c.insets = new Insets(10, 4, 30, 4);
	    add(highScoreLabel, c);
		
		new Insets(4,0,0,4);
		JLabel l = new JLabel();
		c.gridx=0;
		c.gridy=1;
		c.weightx = 1;
		add(l, c);
		
		
		cetiri.setFont(new Font("Arial", Font.BOLD, 16)); // Set font and size
		cetiri.setBackground(new Color(187,173,160));   // Light blue background
		cetiri.setForeground(Color.WHITE);              // White text
		cetiri.setFocusPainted(false);                  // Remove focus border
		cetiri.setBorder(BorderFactory.createCompoundBorder(
		    BorderFactory.createLineBorder(new Color(30, 60, 90), 2), // Outer border
		    BorderFactory.createEmptyBorder(5, 15, 5, 15)            // Padding
		));
		c.gridwidth = 1;
		c.insets = new Insets(4,0,0,4);
		c.gridx=1;
		c.gridy=1;
		add(cetiri, c);
		
		pet.setFont(new Font("Arial", Font.BOLD, 16)); 
		pet.setBackground(new Color(187,173,160));   
		pet.setForeground(Color.WHITE);              
		pet.setFocusPainted(false);                 
		pet.setBorder(BorderFactory.createCompoundBorder(
		    BorderFactory.createLineBorder(new Color(30, 60, 90), 2), 
		    BorderFactory.createEmptyBorder(5, 15, 5, 15)           
		));
		c.gridx=2;
		c.gridy=1;
		add(pet, c);
		
		//Klik na dugme inicijaliziramo određenu igru
		cetiri.addActionListener(e -> Inicijalizacija(4));
	    pet.addActionListener(e -> Inicijalizacija(5)); 
	    
	    //Resetovanje vrijednosti
		c.weightx=0;
		c.weighty=0;
		
		//Default
		setVisible(true);
    }

    
    //Sama igra
    public void Inicijalizacija(int dimenzija) {
    	//Obriši sve što je bilo na meniju
        getContentPane().removeAll();
        repaint();
        revalidate();

        logika = new Logika(dimenzija);
        buttons = new JButton[dimenzija][dimenzija];

        setTitle("2048 Game - " + dimenzija + "x" + dimenzija);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(dimenzija, dimenzija));
        
        //Postavljanje dugmadi na tabelu (grid), mjenja broj i boju dugmadi, dodaje ih u GridPanel.
        for (int i = 0; i < logika.getSirina(); i++) {
            for (int j = 0; j < logika.getVisina(); j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                buttons[i][j].setEnabled(false);
                gridPanel.add(buttons[i][j]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
        
        //Dodajemo da se može vidjeti rezultat tokom igranja
        rezultatLabel = new JLabel("Rezultat: 0");
        rezultatLabel.setFont(new Font("Arial", Font.BOLD, 18));
        rezultatLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(rezultatLabel, BorderLayout.SOUTH);

        //Ako kliknemo strelice
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                
                //Za odrezenji KeyCode radi pomjeranje
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        logika.PomjeriGore();
                        break;
                    case KeyEvent.VK_DOWN:
                        logika.PomjeriDole();
                        break;
                    case KeyEvent.VK_LEFT:
                        logika.PomjeriLijevo();
                        break;
                    case KeyEvent.VK_RIGHT:
                        logika.PomjeriDesno();
                        break;
                }
                //Update tabele nakon svakog pomjeranja/akcije
                updateBoard();
            }
            //Ovo nije potrebno implementirati
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        setFocusable(true);
        requestFocusInWindow();
        //Updatujemo opet tabelu
        updateBoard();
        
        revalidate();
        repaint();
    }



    //Postavljanje dugmadi u odnosu na broj
    private void updateButtonAppearance(JButton button, int value) {
    	button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(187, 173, 160), 4));
        if (value == 0) {
            //Ako je nula prazno je
            button.setText("");
            button.setBackground(new Color(204,192,179));
        } else {
        	//Ako je proj dobija odreženu boju iz funkcije Boja(broj)
            button.setText(String.valueOf(value));
            button.setBackground(Boja(value));
        }
        //Ovo sam uradio da bi text mogao biti bijele boje, da nisam bilo bi sive bez obzira na Foreground
        button.setEnabled(true); 
        button.setFocusable(false);
    }
    
    //Odabir boje u zavisnosti od broja
    private Color Boja(int Broj) {
        if (Broj == 2) {
            return Color.decode("#bab399");
        }
        if (Broj == 4) {
            return Color.decode("#ada378");
        }
        if (Broj == 8) {
            return Color.decode("#b8924b");
        }
        if (Broj == 16) {
            return Color.decode("#ba6c27");
        }
        if (Broj == 32) {
            return Color.decode("#a35034");
        }
        if (Broj == 64) {
            return Color.decode("#b8380d");
        }
        if (Broj == 128) {
            return Color.decode("#deb449");
        }
        if (Broj == 256) {
            return Color.decode("#cca33b");
        }
        if (Broj == 512) {
            return Color.decode("#c79d32");
        }
        if (Broj == 1024) {
            return Color.decode("#cf9f25");
        }
        if (Broj == 2048) {
            return Color.decode("#d19e1d");
        }
        return Color.decode("#000000");
    }

    //Update stanje funkcije nakon svakog dešavanja
    private void updateBoard() {//Mjenja brojeve i boju na dugmadi, u zavisnosti od stanja na tabeli.
        int[][] tabela = logika.getTabela();
        for (int i = 0; i < logika.getSirina(); i++) {
            for (int j = 0; j < logika.getVisina(); j++) {
                int value = tabela[i][j];
                updateButtonAppearance(buttons[i][j], value);
            }
        }

        // Ažuriraj rezultat
        int rezultat = logika.getRezultat(); // logika ima metodu getRezultat()
        rezultatLabel.setText("Rezultat: " + rezultat);
    }

}
