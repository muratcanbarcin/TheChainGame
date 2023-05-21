import enigma.core.Enigma;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.console.TextAttributes;
import java.awt.Color;
import java.util.Random;
import java.util.Scanner;

public class ChainGame {
    public static enigma.console.Console cn = Enigma.getConsole("Chain", 85, 30, 20);
    public KeyListener klis;
    Scanner sc = new Scanner(System.in);
    TextAttributes green = new TextAttributes(Color.GREEN, Color.BLACK);
    // ------ Standard variables for keyboard ------
    public int keypr; // key pressed?
    public int rkey; // key (for press/release)
    public static int seed = 0;
    MultiLinkedList table=new MultiLinkedList();
    SingleLinkedList chain = new SingleLinkedList();
    public static int enterCount=0;

    // ----------------------------------------------------
    ChainGame() throws Exception { // --- Contructor
        // ------ Standard code for mouse and keyboard ------ Do not change

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

        int score = 0;
        int round = 1;
        boolean gameOver = false;
        System.out.print(" 0: Play \n 1: Enter seed and play \n 2: View high score table \n Enter your choice: ");
        int choice = 0;
        try {
            choice = sc.nextInt();
        } catch (Exception e) {
        }

        int seed = 0;
        if (choice == 1) {
            System.out.print(" Please enter a seed(integer) for the board: ");
            try {
                seed = sc.nextInt();
            } catch (Exception e) {
            }
        }
        if (choice == 2) {
            // displayHST(); high-score table
        }

        for (int i = 0; i < 5; i++) {
            cn.getTextWindow().setCursorPosition(0, i);
            cn.getTextWindow()
                    .output("                                                                                ");
        }

        Random rnd = new Random(seed);

        int px = 15, py = 9;
        int xdir = 0, ydir = 0;

        boolean moved = false;

        char[][] gamezone = new char[19][31];

        chain = new SingleLinkedList();

        Player p = new Player(px, py);

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 31; j++) {
                if (i % 2 == 0 && j % 2 == 0)
                    gamezone[i][j] = (char) (1 + rnd.nextInt(4) + '0');
                else
                    gamezone[i][j] = ' ';
                cn.getTextWindow().output(j, i, gamezone[i][j]);
            }
        }

        cn.getTextWindow().output(px, py, 'P', green);

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
                xdir = 0;
                ydir = 0;
                moved = false;
                if (rkey == KeyEvent.VK_LEFT && px > 0)
                    xdir = -1;
                else if (rkey == KeyEvent.VK_RIGHT && px < 30)
                    xdir = 1;
                else if (rkey == KeyEvent.VK_UP && py > 0)
                    ydir = -1;
                else if (rkey == KeyEvent.VK_DOWN && py < 18)
                    ydir = 1;
                else if (rkey == KeyEvent.VK_SPACE) {
                    if (gamezone[py][px] == '+') {
                        cn.getTextWindow().output(px, py, 'P', green);
                        gamezone[py][px] = ' ';
                    } else if (gamezone[py][px] == ' ') {
                        cn.getTextWindow().output(px, py, '+', green);
                        gamezone[py][px] = '+';
                    }
                } else if (rkey == KeyEvent.VK_ENTER) {
                    int x = -1, y = -1; // start coordinates of chain
                    int edgeC = 0;
                    enterCount++;
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 31; j++) {
                            if (i % 2 == 1 && j % 2 == 1 && gamezone[i][j] == '+') // plus on wrong position
                                gameOver = true;
                            if (i % 2 == 0 && j % 2 == 0) {
                                int plusC = 0;
                                if (i > 0 && gamezone[i - 1][j] == '+') {
                                    plusC++;
                                }
                                if (i < 18 && gamezone[i + 1][j] == '+') {
                                    plusC++;
                                }
                                if (j > 0 && gamezone[i][j - 1] == '+') {
                                    plusC++;
                                }
                                if (j < 30 && gamezone[i][j + 1] == '+') {
                                    plusC++;
                                }
                                if (plusC == 1) { // start or end
                                    x = j;
                                    y = i;
                                    edgeC++;
                                } else if (plusC > 2)
                                    gameOver = true; // faulty chain
                            }
                        }
                    }

                    if (edgeC > 2 || edgeC == 0)
                        gameOver = true; // more than 1 chain, no chain

                    if (x == -1 && y == -1) {
                        gameOver = true;
                    }

                    if (gameOver)
                        break;

                    boolean foundNeighbor = true;
                    int prev = 1 + Character.getNumericValue(gamezone[y][x]);
                    while (foundNeighbor) {

                        if (Math.abs(prev - Character.getNumericValue(gamezone[y][x])) != 1) {
                            gameOver = true;
                            break;
                        }

                        chain.add(gamezone[y][x]);
                        prev = Character.getNumericValue(gamezone[y][x]);

                        // showing player the chain in green
                        cn.getTextWindow().output(x, y, gamezone[y][x], green);

                        gamezone[y][x] = '.';

                        if (x + 2 < 31 && gamezone[y][x + 1] == '+' && gamezone[y][x + 2] != '.') { // right neighbor
                            x += 2;
                        } else if (x - 2 >= 0 && gamezone[y][x - 1] == '+' && gamezone[y][x - 2] != '.') { // left
                            // neighbor
                            x -= 2;
                        } else if (y + 2 < 19 && gamezone[y + 1][x] == '+' && gamezone[y + 2][x] != '.') { // bottom
                            // neighbor
                            y += 2;
                        } else if (y - 2 >= 0 && gamezone[y - 1][x] == '+' && gamezone[y - 2][x] != '.') { // top
                            // neighbor
                            y -= 2;
                        } else
                            foundNeighbor = false;
                    }

                    Thread.sleep(500); // showing chain in screen before it turns into dots

                    if (chain.size() < 4)
                        break; // size insufficient

					/*if (round > 1)
						cn.getTextWindow().output(35, 2 + 2 * round, '+'); // printing table (not MLL yet)
					cn.getTextWindow().setCursorPosition(35, 3 + 2 * round);
					chain.display();*/
                    addTable();
                    //cn.getTextWindow().setCursorPosition(35,5);
                    table.display(cn);

                    // calculate score and make chain into dots on board, add to table
                    score += chain.size() * chain.size();
                    round++;
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 31; j++) {
                            if (gamezone[i][j] == '+')
                                gamezone[i][j] = ' ';
                            cn.getTextWindow().output(j, i, gamezone[i][j]);
                        }
                    }
                    cn.getTextWindow().output(px, py, 'P', green);
                    chain = new SingleLinkedList(); // reset chain
                }

                if (gamezone[py + ydir][px + xdir] == ' ' && (xdir != 0 || ydir != 0)) {
                    px += xdir;
                    py += ydir;
                    moved = true;
                    p.setX(px);
                    p.setY(py);
                    cn.getTextWindow().output(px, py, 'P', green);
                } else if (gamezone[py + ydir][px + xdir] == '+' && (xdir != 0 || ydir != 0)) {
                    px += xdir;
                    py += ydir;
                    moved = true;
                    p.setX(px);
                    p.setY(py);
                    cn.getTextWindow().output(px, py, '+', green);
                }

                if (moved) {
                    if (gamezone[py - ydir][px - xdir] == ' ') {
                        cn.getTextWindow().output(px - xdir, py - ydir, ' ');
                    } else if (gamezone[py - ydir][px - xdir] == '+') {
                        cn.getTextWindow().output(px - xdir, py - ydir, '+');
                    }
                }

                keypr = 0;
            }

            cn.getTextWindow().setCursorPosition(47, 1);
            System.out.print(round);
            cn.getTextWindow().setCursorPosition(47, 2);
            System.out.print(score);

            Thread.sleep(50);

        }

        cn.getTextWindow().setCursorPosition(35, 16);
        cn.getTextWindow().output("Error in chain");
        cn.getTextWindow().setCursorPosition(35, 17);
        cn.getTextWindow().output("- Game Over -");

    }

    public void addTable() {
        int size=chain.size();
        table.addCategory(String.valueOf(enterCount));
        for (int i = 1; i < size+1; i++) {
            table.addItem(String.valueOf(enterCount), String.valueOf(chain.findData(i)));
        }
    }

}
