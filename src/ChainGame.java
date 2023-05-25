import enigma.core.Enigma;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.console.TextAttributes;
import java.awt.Color;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class ChainGame {
    public static enigma.console.Console cn = Enigma.getConsole("Chain", 85, 30, 20);
    public KeyListener klis;
    Scanner sc = new Scanner(System.in);
    TextAttributes green = new TextAttributes(Color.GREEN, Color.BLACK);
    TextAttributes pink = new TextAttributes(Color.pink, Color.BLACK);
    // ------ Standard variables for keyboard ------
    public int keypr; // key pressed?
    public int rkey; // key (for press/release)
    public static int seed = 0;
    public static boolean gameOver = false;
    public static int timeunit = 20;
    public static DoubleLinkedList HighScoreTable = new DoubleLinkedList();
    MultiLinkedList table = new MultiLinkedList();
    SingleLinkedList chain = new SingleLinkedList();
    Player p = new Player(15, 9);
    public static int round = 1;
    public static int score = -1;
    public static char[][] board = new char[19][31];;
    public static String name =" ";
    public static boolean exit = true;
    public static String filePath = "highscoretable.txt";
    public static String lineHigh;

    // ----------------------------------------------------
    ChainGame() throws Exception { // --- Contructor
        // ------ Standard code for mouse ------ Do not change

        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }
            public void keyReleased(KeyEvent e) {
            }
        };
        cn.getTextWindow().addKeyListener(klis);
        menu();
        keypr = 0; // so that 'enter' key pressed to exit menu doesn't end the game

        clear(); // clear console
        while (exit){
            name = " ";
            cn.getTextWindow().setCursorPosition(0,0);
            cn.getTextWindow().output("Name:");
            name = cn.readLine();
            keypr = 0;

            score =0;
            String reason = ""; // error reason
            int px = 15, py = 9; // player starts at the center
            int xdir = 0, ydir = 0; // for player movement

            p.setX(px); p.setY(py);

            initializeBoard(p);

            cn.getTextWindow().setCursorPosition(35, 0);
            cn.getTextWindow().output("Board Seed: " + seed);
            cn.getTextWindow().setCursorPosition(35, 1);
            cn.getTextWindow().output("Round     : " + round);
            cn.getTextWindow().setCursorPosition(35, 2);
            cn.getTextWindow().output("Score     : " + score);
            cn.getTextWindow().setCursorPosition(35, 3);
            cn.getTextWindow().output("------------------------------------");
            cn.getTextWindow().setCursorPosition(35, 4);
            cn.getTextWindow().output("Table:");

            while (!gameOver) {

                if (keypr == 1) { // if keyboard button pressed
                    xdir = 0; ydir = 0;
                    if (rkey == KeyEvent.VK_LEFT && px > 0)
                        xdir = -1;
                    else if (rkey == KeyEvent.VK_RIGHT && px < 30)
                        xdir = 1;
                    else if (rkey == KeyEvent.VK_UP && py > 0)
                        ydir = -1;
                    else if (rkey == KeyEvent.VK_DOWN && py < 18)
                        ydir = 1;
                    else if (rkey == KeyEvent.VK_SPACE) {
                        if (board[py][px] == '+') { // + turned into blank
                            board[py][px] = 'P';
                        } else if (board[py][px] == 'P') { // blank turned into +
                            board[py][px] = '+';
                        }
                    } else if (rkey == KeyEvent.VK_ENTER) {
                        int x = -1, y = -1; // start/end coordinates of chain
                        int edgeC = 0; // edge count
                        int size = 0;
                        for (int i = 0; i < 19; i++) {
                            for (int j = 0; j < 31; j++) { // checking chain for errors
                                if (i % 2 == 1 && j % 2 == 1 && board[i][j] == '+') { // there should be no + in odd indexes
                                    reason = "+ sign on wrong pos.(crosswise a number)";
                                    gameOver = true;
                                }
                                else if (i % 2 == 0 && j % 2 == 0) {
                                    int plusC = 0;
                                    if (i > 0 && board[i - 1][j] == '+') plusC++; // top
                                    if (i < 18 && board[i + 1][j] == '+') plusC++; // bottom
                                    if (j > 0 && board[i][j - 1] == '+') plusC++; // left
                                    if (j < 30 && board[i][j + 1] == '+') plusC++; // right
                                    if (plusC == 1) { // start or end
                                        x = j; y = i; // coordinates of start/end
                                        edgeC++;
                                        size++; // one element of chain found at board[i][j]
                                    } else if (plusC > 2) { // faulty chain
                                        reason = "Connection w more than 2 + signs";
                                        gameOver = true;
                                    } else if(plusC == 2) size++; // one element found at board[i][j]
                                }
                            }
                        }

                        if (gameOver) break;

                        if (edgeC > 2) { // more than 2 start-end points found
                            reason = "More than 1 chain";
                            gameOver = true;
                            break;
                        }
                        else if(edgeC == 0) { // no start-end found
                            reason = "No chain";
                            gameOver = true;
                            break;
                        }
                        else if(size < 4) { // using size instead chain.size() because chain isn't constructed yet
                            // and if there is a difference error, we can't see the exact size of chain as it will be game over instantly
                            reason = "Chain size insufficient("+size+" < 4)";
                            gameOver = true;
                            break;
                        }

                        checkNeighbors(x, y); // checking differences between all elements of chain

                        if (gameOver) {  // if there was an error in checkNeighbors function
                            reason = "Diff. of at least 2 neighbors != 1";
                            break;
                        }

                        addTable();

                        table.display(cn);

                        for (int i = 0; i < 19; i++)
                            for (int j = 0; j < 31; j++)
                                if (board[i][j] == '+') board[i][j] = ' '; // new round, plus signs reset

                        score += chain.size() * chain.size();
                        round++;

                        chain = new SingleLinkedList(); // reset chain

                    } else if(rkey == KeyEvent.VK_E) {
                        break;
                    }

                    if (board[py + ydir][px + xdir] == ' ') { // can move to blank spot
                        if(board[py][px] != '+') board[py][px] = ' '; //  prev. location, if not +, turned into blank
                        px += xdir; py += ydir;
                        board[py][px] = 'P'; // P is at the blank spot

                    } else if (board[py + ydir][px + xdir] == '+') {	// can move to + sign
                        if(board[py][px] != '+') board[py][px] = ' '; // prev. location, if not +, turned into blank
                        px += xdir; py += ydir;
                        board[py][px] = '+'; // P is here along with + sign, + prioritized

                    } else if (!((py < 2 && ydir == -1) || (py > 16 && ydir == 1) || (px < 2 && xdir == -1) || (px > 28 && xdir == 1))) {
                        // jump to roam the board faster
                        if(board[py][px] != '+') board[py][px] = ' ';
                        px += 2*xdir; py += 2*ydir;
                        board[py][px] = (board[py][px] == ' ') ? 'P' : '+';  // if new location is blank, it is now p / if it's +, it stays so. + is prioritized
                    }

                    p.setX(px); p.setY(py);

                    printBoard(); // every time unit, game board is printed to visually update the game

                    cn.getTextWindow().setCursorPosition(47, 1);
                    System.out.print(round);
                    cn.getTextWindow().setCursorPosition(47, 2);
                    System.out.print(score);

                    keypr = 0;
                }

                Thread.sleep(timeunit);

            }

            if(gameOver) { // there was an error (user didn't exit mid-game)
                cn.getTextWindow().setCursorPosition(45, 16);
                cn.getTextWindow().output("Error in chain");
                cn.getTextWindow().setCursorPosition(45, 17);
                cn.getTextWindow().output("- Game Over -");
                cn.getTextWindow().setCursorPosition(45, 18);
                cn.getTextWindow().output(reason, pink);
                cn.getTextWindow().setCursorPosition(45, 19);
                cn.getTextWindow().output("Press E to exit", green);
                while (rkey != KeyEvent.VK_E) {
                    Thread.sleep(50);
                    keypr=0;
                }
            }
            gameOver = false;

            table = new MultiLinkedList();
            if(!((name.trim().equalsIgnoreCase("")) || score == -1)) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
                    writer.newLine();
                    writer.write(name + " " + score);
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Dosyaya veri eklenirken bir hata oluştu: " + e.getMessage());
                }
            }
            cn.getTextWindow().setCursorPosition(0,0);
            //Ayırıp sonrasında SLL3 ve SLL4'e atamak
            HighScoreTable = new DoubleLinkedList();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                while ((lineHigh = br.readLine()) != null) {
                    String strName = lineHigh.substring(0, lineHigh.lastIndexOf(" ")).trim();
                    String strNumber = lineHigh.substring(lineHigh.lastIndexOf(" ") + 1);
                    HighScoreTable.add(strName);
                    HighScoreTable.add(strNumber);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            clear();
            cn.getTextWindow().setCursorPosition(0,0);
            HighScoreTable.sorted();
            HighScoreTable.display();

            cn.getTextWindow().setCursorPosition(45,20);
            cn.getTextWindow().output("Press enter to return to menu", green);
            cn.readLine();

            clear();
            cn.getTextWindow().setCursorPosition(0,0);
            menu();
            keypr = 0; // so that 'enter' key pressed to exit menu doesn't end the game

            clear(); // clear console
        }

    }

    public void printBoard() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 31; j++) {
                if(board[i][j] == 'P' || (board[i][j] == '+' && i==p.getY() && j==p.getX()))
                    cn.getTextWindow().output(j, i, board[i][j], green); // print P or + green when P is on a + sign
                else cn.getTextWindow().output(j, i, board[i][j]); // print def color

            }
        }
    }

    public void checkNeighbors(int x, int y) {
        boolean foundNeighbor = true;
        int prev = 1 + Character.getNumericValue(board[y][x]); // previous element in chain
        while (foundNeighbor) {

            if (Math.abs(prev - Character.getNumericValue(board[y][x])) != 1) { // difference should be -1 or 1
                gameOver = true;
                return; // so that it doesn't go on unnecessarily, main while loop will break below the line of call
            }

            chain.add(board[y][x]); // found correct element added to chain

            prev = Character.getNumericValue(board[y][x]);

            board[y][x] = '.'; // checked part of the chain turned into dot
            // traversing connected elements till no more can be found
            if (x + 2 < 31 && board[y][x + 1] == '+' && board[y][x + 2] != '.') { // right neighbor
                x += 2;
            } else if (x - 2 >= 0 && board[y][x - 1] == '+' && board[y][x - 2] != '.') { // left																											// neighbor
                x -= 2;
            } else if (y + 2 < 19 && board[y + 1][x] == '+' && board[y + 2][x] != '.') { // bottom																										// neighbor
                y += 2;
            } else if (y - 2 >= 0 && board[y - 1][x] == '+' && board[y - 2][x] != '.') { // top																											// neighbor
                y -= 2;
            } else
                foundNeighbor = false;
        }
    }

    public void addTable() {
        int size=chain.size();
        table.addCategory(String.valueOf(round));
        for (int i = 1; i < size+1; i++) {
            table.addItem(String.valueOf(round), String.valueOf(chain.findData(i)));
        }
    }

    public void clear() {
        for (int i = 0; i < cn.getTextWindow().getRows(); i++)
            for (int j = 0; j < cn.getTextWindow().getColumns(); j++)
                cn.getTextWindow().output(j, i, ' ');
    }

    public void initializeBoard(Player p) {
        Random rnd = new Random(seed);
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 31; j++) {
                if (i % 2 == 0 && j % 2 == 0) board[i][j] = (char) (1 + rnd.nextInt(4) + '0');
                else board[i][j] = ' ';
                cn.getTextWindow().output(j, i, board[i][j]);
            }
        }
        board[p.getY()][p.getX()] = 'P'; cn.getTextWindow().output(p.getX(), p.getY(), 'P', green); // p put on board/printed
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
            cn.getTextWindow().output("->", green);

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

                cn.getTextWindow().output("\n \nTime Unit(25(ms)-1000(ms)):");
                try {
                    while (!(timeunit>=25 && timeunit<=1000))
                    {
                        timeunit = sc.nextInt();
                    }
                }
                catch(Exception e) {}

                break;
            case 3:
                // High Score Table seçeneği seçildiğinde yapılacak işlemler
                clear();
                cn.getTextWindow().setCursorPosition(0,0);

                //HighScoreTable.txt read
                HighScoreTable = new DoubleLinkedList();
                //Ayırıp sonrasında SLL3 ve SLL4'e atamak
                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                    while ((lineHigh = br.readLine()) != null) {
                        String strName = lineHigh.substring(0, lineHigh.lastIndexOf(" ")).trim();
                        String strNumber = lineHigh.substring(lineHigh.lastIndexOf(" ") + 1);
                        HighScoreTable.add(strName);
                        HighScoreTable.add(strNumber);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clear();
                cn.getTextWindow().setCursorPosition(0,0);
                HighScoreTable.sorted();
                HighScoreTable.display();
                cn.getTextWindow().output("\nPress enter to exit.");
                cn.readLine();
            case 4:
                // Exit seçeneği seçildiğinde yapılacak işlemler
                clear();
                cn.getTextWindow().setCursorPosition(0,0);
                cn.getTextWindow().output("You Exited the Game");
                exit = false;
                break;
        }
    }
}
