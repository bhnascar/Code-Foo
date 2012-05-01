import java.awt.*;
import javax.swing.*;

/* The Board class represents a game board with n x n squares.
 * The squares can contain game pieces (in TicTacToe, Marks), which
 * are governed by a set of external game controller, defined in the Controller class.
 */
@SuppressWarnings("serial")
public class Board extends JPanel
{
    private Square[][] squares;
    private Controller controller;
    
    /* Creates a square game board with user-defined dimensions */
    public Board(Controller c, int width, int height) {       
        controller = c;
        setDimensions(width, height);
    }
    
    /* Resizes board */
    public void setDimensions(int width, int height) {
        removeAll();
        setLayout(new GridLayout(height, width));
        setSize(new Dimension(width * Square.SQUARE_SIZE, height * Square.SQUARE_SIZE));
        setPreferredSize(new Dimension(width * Square.SQUARE_SIZE, height * Square.SQUARE_SIZE));
        
        squares = new Square[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                squares[y][x] = new Square(controller, x, y);
                add(squares[y][x]);
            }
        }
        
        controller.reset(false, false);
        revalidate();
        repaint();
    }
    
    /* Determines if a location is legal */
    public boolean isInBounds(int x, int y) {
        return y >= 0 &&
        	   y < squares.length &&
        	   x >= 0 &&
        	   x < squares[y].length;
    }
    
    /* Clears the board of all Marks; e.g. empties Squares */
    public void clear() {
        for(Square[] row : squares) {
            for(Square s : row) {
                 s.setMark(null);
                 s.setIsWinner(false);
            }
        }
        repaint();
    }
    
    
    /* Returns the Square at a location on the board */
    public Square getSquareAt(int x, int y) {
    	if (!isInBounds(x, y)) return null;
    	return squares[y][x]; 
    }
        
    /* Returns the board width */
    public int getBoardWidth() { return squares[0].length; }
    
    /* Returns the board width */
    public int getBoardHeight() { return squares.length; }
    
    /* Returns the all the board Squares */
    public Square[][] getSquares() { return squares; }
    
    /* Returns the controller of the board (game controller) */
    public Controller getController() { return controller; }
}