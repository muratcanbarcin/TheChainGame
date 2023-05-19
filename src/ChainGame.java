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
    public static enigma.console.Console cn = Enigma.getConsole("Chain",75,21,15);
    public static SingleLinkedList chain = new SingleLinkedList();
    public static DoubleLinkedList HST =new DoubleLinkedList();
    public static MultiLinkedList table = new MultiLinkedList();
    public KeyListener klis;
    public TextMouseListener tmlis;


    Scanner sc = new Scanner(System.in);
    TextAttributes green = new TextAttributes(Color.GREEN, Color.BLACK);
    // ------ Standard variables for keyboard ------
    public int keypr;   // key pressed?
    public int rkey;    // key   (for press/release)
    /*
                Mouse ile ilgili çalışmaları yap

    public int mousepr;          // mouse pressed?
    public int mousex, mousey;   // mouse text coords.
     */
    // ----------------------------------------------------
    ChainGame() throws Exception {   // --- Contructor
        // ------ Standard code for mouse and keyboard ------ Do not change
       /*
        tmlis=new TextMouseListener() {
            public void mouseClicked(TextMouseEvent arg0) {}
            public void mousePressed(TextMouseEvent arg0) {
                if(mousepr==0) {
                    mousepr=1;
                    mousex=arg0.getX();
                    mousey=arg0.getY();
                }
            }
            public void mouseReleased(TextMouseEvent arg0) {}
        };
        cn.getTextWindow().addTextMouseListener(tmlis);

        */

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

        int score = 0;
        int round = 1;
        boolean gameOver = false;
        cn.getTextWindow().output(" 1: Play \n 2: Enter seed \n 3: View high score table \n Enter your choice: ");
        int choice = 1;
        try {
            choice = sc.nextInt();
        }
        catch(Exception e) {}

        int seed = 0;
        if(choice==2) {
            clear();
            cn.getTextWindow().setCursorPosition(0,0);
            cn.getTextWindow().output(" Please enter a seed(integer) for the board: "); //Player select a seed of random
            try {
                seed = sc.nextInt();
            }
            catch(Exception e) {}
        }
        if(choice==3) {
            //displayHST(); high-score table
        }

        for(int i=0; i<5; i++) {
            cn.getTextWindow().setCursorPosition(0, i);
            cn.getTextWindow().output("                                                                                ");
        }

        Random rnd = new Random(seed);

        int px=15, py=9;
        int xdir = 0, ydir = 0;

        boolean moved = false;

        //Create Game Area
        char[][] gamezone = new char [19][31];

        //   Player p = new Player(px, py);

        for(int i=0; i<19; i++) {
            for(int j=0; j<31; j++) {
                if(i%2==0 && j%2==0) gamezone[i][j] = (char) (1+rnd.nextInt(4)+'0');
                else gamezone[i][j] = ' ';
                cn.getTextWindow().output(j, i, gamezone[i][j]);
            }
        }
        cn.getTextWindow().output(px, py, 'P', green);

        cn.getTextWindow().setCursorPosition(35, 0);
        cn.getTextWindow().output("Board Seed: "+seed);
        cn.getTextWindow().setCursorPosition(35, 1);
        cn.getTextWindow().output("Round     : "+round);
        cn.getTextWindow().setCursorPosition(35, 2);
        cn.getTextWindow().output("Score     : "+score);
        cn.getTextWindow().setCursorPosition(35, 3);
        cn.getTextWindow().output("------------------------------------");
        cn.getTextWindow().setCursorPosition(35, 4);
        cn.getTextWindow().output("Table:");

        while(!gameOver) {
/*
            Mouse ile ilgili çalışmaları yap

            if(mousepr==1) {  // if mouse button pressed
                cn.getTextWindow().output(mousex,mousey,'#');  // write a char to x,y position without changing cursor position
                px=mousex; py=mousey;

                mousepr=0;     // last action
            }
*/
            if(keypr==1) {    // if keyboard button pressed
                xdir=0;
                ydir=0;
                moved = false;
                if(rkey==KeyEvent.VK_LEFT && px > 0) xdir = -1;
                else if(rkey==KeyEvent.VK_RIGHT && px < 30) xdir = 1;
                else if(rkey==KeyEvent.VK_UP && py > 0) ydir = -1;
                else if(rkey==KeyEvent.VK_DOWN && py < 18) ydir = 1;
                else if(rkey==KeyEvent.VK_SPACE) {
                    if(gamezone[py][px] == '+') {
                        cn.getTextWindow().output(px, py, 'P', green);
                        gamezone[py][px] = ' ';
                    }
                    else if(gamezone[py][px] == ' ')
                    {
                        cn.getTextWindow().output(px, py, '+', green);
                        gamezone[py][px] = '+';
                    }
                }
                else if(rkey==KeyEvent.VK_ENTER) {
                    int edgeC = 0; //chain number
                    for(int i=0; i<19; i++) {
                        for(int j=0; j<31; j++) {
                            if(i%2==1 && j%2==1 && gamezone[i][j] == '+')
                                gameOver = true;
                            if(i%2==0 && j%2==0) {
                                int plusC = 0;
                                if(i>0 && gamezone[i-1][j] == '+') {

                                    plusC++;

                                }
                                if(i<18 && gamezone[i+1][j] == '+') {

                                    plusC++;

                                }
                                if(j>0 && gamezone[i][j-1] == '+') {

                                    plusC++;

                                }
                                if(j<30 && gamezone[i][j+1] == '+') {

                                    plusC++;

                                }
                                if(plusC == 1) {
                                    edgeC++;
                                    chain.add(gamezone[i][j]); // start or end
                                }
                                else if(plusC == 2) chain.add(gamezone[i][j]);
                                else if(plusC > 2) gameOver = true; // faulty chain
                            }
                        }
                    }

                    if(edgeC > 2 || edgeC == 0) gameOver = true; // more than 1 chain, no chain
                    if(chain.size() < 4) gameOver = true; // size insufficient

                    cn.getTextWindow().setCursorPosition(45, 11);
                    cn.getTextWindow().output("Size: "+chain.size());
                    cn.getTextWindow().setCursorPosition(45, 12);
                    displaySLL(); // order is wrong, displays row to row, not linked
                    cn.getTextWindow().setCursorPosition(45, 4);
                    displayMLL();


                    // calculate score and make chain into dots on board, add to table
                    if(!gameOver) {
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


                if(gamezone[py+ydir][px+xdir] == ' ' && (xdir != 0 || ydir != 0)) {
                    px+=xdir; py+=ydir;
                    moved = true;
                    cn.getTextWindow().output(px, py, 'P', green);
                }
                else if(gamezone[py+ydir][px+xdir] == '+' && (xdir != 0 || ydir != 0)) {
                    px+=xdir; py+=ydir;
                    moved = true;
                    cn.getTextWindow().output(px, py, '+', green);
                }

                if(moved) {
                    if(gamezone[py-ydir][px-xdir] == ' ') {
                        cn.getTextWindow().output(px-xdir, py-ydir, ' ');
                    }
                    else if(gamezone[py-ydir][px-xdir] == '+') {
                        cn.getTextWindow().output(px-xdir, py-ydir, '+');
                    }
                }

                keypr=0;
            }

            cn.getTextWindow().setCursorPosition(47, 1);
            cn.getTextWindow().output(round +" ");
            cn.getTextWindow().setCursorPosition(47, 2);
            cn.getTextWindow().output(score+ " ");

            Thread.sleep(50);

        }



    }

    public void clear() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                cn.getTextWindow().setCursorPosition(i,j);
                cn.getTextWindow().output(" ");
            }
        }
        cn.getTextWindow().setCursorPosition(0,0);
    }

    public void displayMLL(){
        if(table.head == null)
            cn.getTextWindow().output("linked list is empty");
        else {
            ColumnNode temp = table.head;
            while (temp != null) {
                cn.getTextWindow().output(temp.getFirstItem());
                RowNode temp2 = temp.getRight();
                while (temp2 != null) {
                    cn.getTextWindow().output(temp2.getItemName() + "+");
                    temp2 = temp2.getNext();
                }
                temp=temp.getDown();
                cn.getTextWindow().output("\n + \n");
            }
        }
    }

    public void displaySLL()
    {
        if(chain.head == null)
            cn.getTextWindow().output("linked list is empty");
        else {
            Node temp = chain.head;
            while (temp != null)
            {
                cn.getTextWindow().output(temp.getData() + " ");
                temp = temp.getLink();
            }
        }
    }

    public void displayDLL() {
        DLLNode currentNode = HST.head;

        while (currentNode != null) {
            cn.getTextWindow().output(currentNode.getData() + " ");
            currentNode = currentNode.getNext();
        }
        cn.getTextWindow().output("\n");
    }


}
