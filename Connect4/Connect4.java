import javax.swing.*;

/*The GameFrame class sets up a game window. The window contains a game board with a set of rules
 * (Board + BoardRule), and a menu bar (Menu).
 */
@SuppressWarnings("serial")
public class Connect4 extends JFrame
{
	private Controller controller;
    private JPanel wrapper;

    public static void main(String[] args) {
        Connect4 g = new Connect4();
        g.setLocationRelativeTo(null);
    }
    
    /*Creates a new game window*/
    public Connect4() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
            catch (Exception e2) {}
        }
        catch (Exception e) {
        	System.out.println("Error setting application look and feel.");
        }
        setUp();
    }

    
    /*Sets up board and menu*/
    public void setUp() {
        controller = new Controller();
        Board board = controller.getBoard();
        getContentPane().add(board);
        
        setJMenuBar(new Menu(controller, this));
        setTitle("Connect 4");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
    
    /*Resizes frame appropriately to show the whole board if the board is resized*/
    public void resize() {
        wrapper.setSize(controller.getBoard().getSize());
        pack();
    }
}