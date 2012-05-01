import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/*The Square class represents a square of a game board (represented by
 * the Board class). Each Square can be empty or contain a Mark (a game piece,
 * as represented by the Mark class). A Square can detect if it is clicked by
 * the player.
 */
@SuppressWarnings("serial")
public class Square extends JPanel implements MouseListener
{
    public static final int SQUARE_SIZE = 100;
    
    private Controller controller;
    private Mark mark;
    private boolean pressed = false, isWinner = false;
    private int x, y;
    
    /* Instance field for Square background images */
    public static ImageIcon bgPressed, background, circleWinBackground, crossWinBackground;

    /* Creates a new empty square on a board. The Board and the an array representing the Square's location on the Board are passed into the parameter */
    public Square(Controller c, int x, int y) {
        controller = c;
        this.x = x;
        this.y = y;
        
        addMouseListener(this);
    }
    
    /* Adds a mark to a Square if it is not empty and the game is not over, then checks for any in-a-rows */
    public void mouseClicked (MouseEvent e) {
    	if (mark != null || !controller.isLegal(x, y) || controller.isGameOver() == true) return;
    	controller.processTurn(this);
    }

    /* When mouse is pressed on a Square, repaints Square with an image of a pressed square */
    public void mousePressed (MouseEvent e) {
        if (mark != null || controller.isGameOver() == true) return;
        pressed = true;
        repaint();
    }
    
    /* When mouse is released from a Square, repaints Square with an image of a normal square */
    public void mouseReleased (MouseEvent e) {
        if (mark != null || controller.isGameOver() == true) return;
        pressed = false;
        repaint();
    }    
    
    public void mouseEntered (MouseEvent e) {}
    
    public void mouseExited (MouseEvent e) {}
    
    /* Paints square */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (pressed && bgPressed != null) { //if the Square is pressed, paints a pressed background
        	g2D.drawImage(bgPressed.getImage(), 0, 0, getWidth(), getHeight(), null);
        }
        else if (isWinner) { //if the Square is part of a winning combo, paints a winner background
            if (mark.getType() == Mark.type.CROSS && crossWinBackground != null)
            	g2D.drawImage(crossWinBackground.getImage(), 0, 0, getWidth(), getHeight(), null);
            else if (circleWinBackground != null)
            	g2D.drawImage(circleWinBackground.getImage(), 0, 0, getWidth(), getHeight(), null);
        }
        else if (background != null && circleWinBackground != null) { //if the Square is normal
        	g2D.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
        }
        //if the Square's mark variable is not null, e.g. it contains a Mark, then paint the mark
        if (mark != null && !isWinner) {
        	g2D.drawImage(mark.getMark().getImage(), 0, 0, getWidth(), getHeight(), null);
        }
    }
    
    /* Sets isWinner - whether or not the Square is part of a winning combo */
    public void setIsWinner(boolean b) { isWinner = b; }
    
    /* Removes the square's mark */
    public void setMark(Mark m) { mark = m; }
    
    /* Returns the square's mark */
    public Mark getMark() { return mark; }
    
    /* Returns the square's x-coordinate */
    public int getBoardX() { return x; }
    
    /* Returns the square's y-coordinate */
    public int getBoardY() { return y; }
}