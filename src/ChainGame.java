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
    public KeyListener klis;
    Scanner sc = new Scanner(System.in);
    TextAttributes green = new TextAttributes(Color.GREEN, Color.BLACK);

    // ------ Standard variables for keyboard ------
    public int keypr;   // key pressed?
    public int rkey;    // key   (for press/release)
    // ----------------------------------------------------

    public ChainGame() throws Exception{  // --- Contructor

    }

}