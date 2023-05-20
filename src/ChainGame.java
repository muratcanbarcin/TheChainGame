import enigma.core.Enigma;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.console.TextAttributes;

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


        fillingBoard();

        while (!gameover) {
            if (keypr == 1) {    // if keyboard button pressed
                xdir = 0;
                ydir = 0;
                if (rkey == KeyEvent.VK_LEFT && px > 0) xdir = -1;
                else if (rkey == KeyEvent.VK_RIGHT && px < 30) xdir = 1;
                else if (rkey == KeyEvent.VK_UP && py > 0) ydir = -1;
                else if (rkey == KeyEvent.VK_DOWN && py < 18) ydir = 1;
                else if (rkey == KeyEvent.VK_SPACE) {
                    if (board[py][px] == '+') {
                        board[py][px] = ' ';
                    }
                    else if (board[py][px] == ' ') {
                        board[py][px] = '+';
                    }
                }
            }
            else if(rkey==KeyEvent.VK_ENTER) {
                int edgeC = 0; //chain number
                for(int i=0; i<19; i++) {
                    for(int j=0; j<31; j++) {
                        if(i%2==1 && j%2==1 && board[i][j] == '+') {
                            gameover = true;
                        }
                        else if(i%2==0 && j%2==0) {
                            int plusC = 0;
                            if(i>0 && board[i-1][j] == '+') {

                                plusC++;

                            }
                            if(i<18 && board[i+1][j] == '+') {

                                plusC++;

                            }
                            if(j>0 && board[i][j-1] == '+') {

                                plusC++;

                            }
                            if(j<30 && board[i][j+1] == '+') {

                                plusC++;

                            }
                            if(plusC == 1) {
                                edgeC++;
                                chain.add(board[i][j]); // start or end
                            }
                            else if(plusC == 2) {
                                chain.add(board[i][j]);
                            }
                            else if(plusC > 2) {
                                gameover = true; // faulty chain
                            }
                        }
                    }
                }

                if(edgeC > 2 || edgeC == 0) gameover = true; // more than 1 chain, no chain
                if(chain.size() < 4) gameover = true; // size insufficient

                //Oyun Alanı Görüntület

                // calculate score and make chain into dots on board, add to table
                if(!gameover) {
                    score += chain.size()*chain.size();
                    table.addColumn(Integer.toString(round));
                    for(int add =0; add<chain.size();add++){
                        Node temp = chain.head;
                        while (temp != null)
                        {
                            table.addItem(Integer.toString(round),temp.getData().toString());
                            temp = temp.getLink();
                        }
                    }
                    round++;
                }
                else {
                    clear();
                    cn.getTextWindow().setCursorPosition(0, 0);
                    cn.getTextWindow().output("GAME OVER");
                    break;
                }
            }


            if(board[py+ydir][px+xdir] == ' ' && (xdir != 0 || ydir != 0)) {
                board[py][px]= ' ';
                px+=xdir; py+=ydir;
                board[py][px] = 'P';
            }
            else if(board[py+ydir][px+xdir] == '+' && (xdir != 0 || ydir != 0)) {
                px+=xdir; py+=ydir;
                cn.getTextWindow().output(px, py, 'P', green);
            }


            Thread.sleep(50);

        }

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

    public void fillingBoard() {

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