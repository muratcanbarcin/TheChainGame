import enigma.core.Enigma;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.console.TextAttributes;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import java.awt.Color;
import java.util.Random;
import java.util.Scanner;

public class ChainGame {
    //Attributes
    public static enigma.console.Console cn = Enigma.getConsole("Chain",75,21,15);
    public static SingleLinkedList chain = new SingleLinkedList();
    public static DoubleLinkedList HST =new DoubleLinkedList();
    public static MultiLinkedList table = new MultiLinkedList();
    public static char[][] board = new  char[19][31];
    public KeyListener klis;
    Scanner sc = new Scanner(System.in);
    TextAttributes green = new TextAttributes(Color.GREEN, Color.BLACK);

    // ------ Standard variables for keyboard ------
    public int keypr;   // key pressed?
    public int rkey;    // key   (for press/release)
    // ----------------------------------------------------

    //Variables
    public static int score =0;
    public static int round =1;
    public static int seed= 0;
    public static boolean gameover =false;
    public static int px=15, py=9;



    public ChainGame() throws Exception{  // --- Contructor

        // ------ Standard code for keyboard ------ Do not change
        klis=new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                if(keypr==0) {
                    keypr=1;
                    rkey=e.getKeyCode();
                }
            }
            public void keyReleased(KeyEvent e) {}
        };
        cn.getTextWindow().addKeyListener(klis);
        // ----------------------------------------------------
        menu();

        int xdir = 0, ydir = 0;

        boolean moved = false;


    }

    public void menu() {
        int selectedOption = 1; // Seçili olan seçeneği tutan değişken
        boolean menuSelected = false; // Menü seçildi mi?

        while (!menuSelected) {
            cn.getTextWindow().setCursorPosition(0, 0);
            cn.getTextWindow().output("  Play");
            cn.getTextWindow().setCursorPosition(0, 1);
            cn.getTextWindow().output("  Settings");
            cn.getTextWindow().setCursorPosition(0, 2);
            cn.getTextWindow().output("  High Score Table");
            cn.getTextWindow().setCursorPosition(0, 3);
            cn.getTextWindow().output("  Exit");

            cn.getTextWindow().setCursorPosition(0, selectedOption - 1);
            cn.getTextWindow().output("->");

            keypr = 0;
            while (keypr == 0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (rkey == KeyEvent.VK_DOWN) {
                selectedOption++;
                if (selectedOption > 4) {
                    selectedOption = 1;
                }
            } else if (rkey == KeyEvent.VK_UP) {
                selectedOption--;
                if (selectedOption < 1) {
                    selectedOption = 4;
                }
            } else if (rkey == KeyEvent.VK_ENTER) {
                menuSelected = true;
            }
        }

        // Seçilen seçenek üzerinde işlem yapılabilir
        switch (selectedOption) {
            case 1:
                // Play seçeneği seçildiğinde yapılacak işlemler

                break;
            case 2:
                // Settings seçeneği seçildiğinde yapılacak işlemler
                clear();
                cn.getTextWindow().setCursorPosition(0,0);
                cn.getTextWindow().output("Seed:");
                try {
                    seed = sc.nextInt();
                }
                catch(Exception e) {}
                break;
            case 3:
                // High Score Table seçeneği seçildiğinde yapılacak işlemler
                break;
            case 4:
                // Exit seçeneği seçildiğinde yapılacak işlemler
                break;
        }
    }

    public void fillingArea() {

        Random rnd = new Random(seed);

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 31; j++) {
                if (i % 2 == 0 && j % 2 == 0) board[i][j] = (char) (1 + rnd.nextInt(4) + '0');
                else board[i][j] = ' ';
            }
        }
        board[px][py] ='P';

    }


    public void clear() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                cn.getTextWindow().output(' ');
            }
        }
    }


}