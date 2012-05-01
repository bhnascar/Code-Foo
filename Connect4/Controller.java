import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

import sun.audio.*;

/* The Controller class represents a set of game rules to be used along with
 * a board (represented by the Board class) to implement a game, e.g. Connect 4. It processes 
 * information about the location of pieces on the board to detect when the game 
 * is over.
 */ 
public class Controller
{
    public static ClassLoader loader = ClassLoader.getSystemClassLoader();
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int TOP = 4;
	public static final int BOTTOM = 8;
	
    /* Instance field for implementing one player or two player games */
    private static boolean playerOnesTurn = true;
    private boolean lastMoveWasPlayer = false;
    private boolean isTie = false;
    private int numberOfPlayers = 1;
    private ArrayList<Square> winners;
    private AI ai;
    
    /* Instance field for game statistics */
    private static int numMoves = 0, numPlayed = 0, numWon = 0, numDrawn = 0;
    private static int winPercent = 0, drawPercent = 0;
    private static int winStreak = 0, drawStreak = 0, loseStreak = 0, currentStreak = 0;
    private boolean wonLastGame = false, tieLastGame = false;
   
    /* Other necessary variables, e.g. for interacting with a Board, for game settings */
    private Board board;
    public static int numToWin = 4;
    private static boolean isGameOver = false;
    private InputStream in; 
    private AudioStream stream;
    
    /* Creates a board rule (game rules) for a given Board */
    public Controller() {
        board = new Board(this, 7, 6);
        ai = new AI(this);
        winners = new ArrayList<Square>();
        setTheme("default");
        try {
            in = loader.getResource(".\\themes\\win.wav").openStream();
            stream = new AudioStream(in);
        }
        catch (Exception e) {
        	System.out.println("Error loading sound clips.");
        }
     }
    
    /* Displays "Statistics" popup */
    public void displayStatistics() {
        JDialog statistics = new JDialog(new JFrame(), "Connect 4 Statistics", true);
        Container content = statistics.getContentPane();
        content.setLayout(new GridLayout(11, 1));
        
        String streakDescription = "      Current streak: " + currentStreak;
        if (!isTie && wonLastGame) {
            if (currentStreak > 1) streakDescription += " wins";
            else streakDescription += " win";
        }
        else if (!isTie && !wonLastGame && numPlayed > 0) {
            if (currentStreak > 1) streakDescription += " losses";
            else streakDescription += " loss";
        }
        else if (isTie) {
            if (currentStreak > 1) streakDescription += " draws";
            else streakDescription += " draw";
        }
        
        JLabel cStreak = new JLabel(streakDescription);
        content.add(new JLabel());
        content.add(new JLabel("      Games played: " + numPlayed));
        content.add(new JLabel("      Games won: " + numWon));
        content.add(new JLabel("      Win percentage: " + winPercent + "%"));
        content.add(new JLabel("      Games drawn: " + numDrawn));
        content.add(new JLabel("      Draw percentage: " + drawPercent + "%"));
        content.add(new JLabel("      Longest winning streak: " + winStreak));
        content.add(new JLabel("      Longest drawing streak: " + drawStreak));
        content.add(new JLabel("      Longest losing streak: " + loseStreak));
        content.add(cStreak);
        
        statistics.setSize(new Dimension(200, 230));
        statistics.setLocationRelativeTo(statistics);
        statistics.setResizable(false);
        statistics.setVisible(true);
    }
    
    /* Adds a piece and then checks the game state */
    public void processTurn(Square square) {
    	numMoves++;
        if (numberOfPlayers == 2) { // Two player game
            if (playerOnesTurn) square.setMark(new Mark(Mark.type.CROSS));
            else square.setMark(new Mark(Mark.type.CIRCLE));  
            square.repaint();
            
            playerOnesTurn = !playerOnesTurn;
            processGameState(square);
        }
        else { // Single player game
        	square.setMark(new Mark(Mark.type.CROSS));
        	square.repaint();
            
            lastMoveWasPlayer = true;
            processGameState(square);
            
            // Move AI
            if (!isGameOver) {
                Square s = getAI().move(square);               
                if (s != null) {
                    s.setMark(new Mark(Mark.type.CIRCLE));
                    s.repaint();
                	lastMoveWasPlayer = false;
                	numMoves++;
                    processGameState(s);
                }
                else tieLastGame = true;
            }
        }
    }
    
    /* Checks if the game is over */
    public void processGameState(Square square) {
        if (check(square) >= numToWin) {
        	highlightWinningSquares();
            reset(true, false);
            updateStats();
            AudioPlayer.player.start(stream);
            if (numberOfPlayers == 2) {
                if (!playerOnesTurn)
                    JOptionPane.showMessageDialog(null, "Player One wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "Player Two wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                if (lastMoveWasPlayer)
                    JOptionPane.showMessageDialog(null, "You win!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "You lose!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (numMoves == board.getBoardWidth() * board.getBoardHeight()) {
            reset(true, true);
            updateStats();
            JOptionPane.showMessageDialog(null, "Tie game!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /* Highlights the winning combination */
    public void highlightWinningSquares() {
    	for (Square s: winners) {
    		s.setIsWinner(true);
    		s.repaint();
    	}
    }
    
    /* Sets the state of the game to over or not over */
    public void reset(boolean isOver, boolean tie) {
        isGameOver = isOver;
        isTie = tie;
        numMoves = 0;
        playerOnesTurn = true;
    }
    
    
    /* Updates game statistics */
    private void updateStats() {
    	// Only keep stats for 1 player games
    	if (numberOfPlayers != 1) return;
    	
        numPlayed++;
        if (!isTie) {
            if (lastMoveWasPlayer) {
                numWon++;
                if (wonLastGame) currentStreak++;
                else {
                    currentStreak = 1;
                    wonLastGame = true;
                    tieLastGame = false;
                }
                if (currentStreak > winStreak) winStreak = currentStreak;
            }
            else {
                if (!wonLastGame) currentStreak++;
                else {
                    currentStreak = 1;
                    wonLastGame = false;
                    tieLastGame = false;
                }
                if (currentStreak > loseStreak) loseStreak = currentStreak;
            }
            winPercent = (int)(((double)(numWon) / numPlayed) *100);
        }
        else {
            numDrawn++;
            if (tieLastGame) currentStreak++;
            else {
                currentStreak = 1;
                tieLastGame = true;
            }
            if (currentStreak > drawStreak) drawStreak = currentStreak;
            drawPercent = (int)(((double)(numDrawn) / numPlayed) *100);
        }
    }
    
    /* Checks if the location (x, y) on the board represents a legal move */
    public boolean isLegal(int x, int y) {
    	if (board.isInBounds(x, y + 1)) {
    		Square s = board.getSquareAt(x, y + 1);
    		if (s.getMark() != null) return true;
    		else return false;
    	}
    	return true; // Bottom row
    }
    
    /* Check if this square is part of a winning combo and returns the number in a row if so */
    public int check(Square s) {
    	winners.clear();
    	int horizontal = 1 + check(s, LEFT) + check(s, RIGHT);
    	if (horizontal >= numToWin) return horizontal;
    	
    	winners.clear();
    	int vertical = 1 + check(s, TOP) + check(s, BOTTOM);
    	if (vertical >= numToWin) return vertical;
    	
    	winners.clear();
    	int	diagonal = 1 + check(s, TOP | LEFT) + check(s, BOTTOM | RIGHT);
    	if (diagonal >= numToWin) return diagonal;
    	
    	winners.clear();
    	int diagonal2 = 1 + check(s, TOP | RIGHT) + check(s, BOTTOM | LEFT);
    	if (diagonal2 >= numToWin) return diagonal2;
    	
    	return -1;
    }
    
    /* Checks for in-a-rows in a specified direction */
    private int check(Square s, int direction) {
    	winners.add(s);
        int x = s.getBoardX(); 
        int y = s.getBoardY();
        
        if ((direction & LEFT) == LEFT) x--;
        if ((direction & RIGHT) == RIGHT) x++;
        if ((direction & TOP) == TOP) y--;
        if ((direction & BOTTOM) == BOTTOM) y++;
        
        if (board.isInBounds(x, y)) {
            Square next = board.getSquareAt(x, y);
            if (next.getMark() != null && next.getMark().equals(s.getMark())) {
                return 1 + check(next, direction);
            }
        }
        return 0;
    }
    
    /* Sets the theme */
    public void setTheme(String theme) {
    	Square.bgPressed = new ImageIcon(loader.getResource("themes/" + theme + "/background_pressed.png"));
    	Square.background = new ImageIcon(loader.getResource("themes/" + theme + "/background.png"));
    	Square.crossWinBackground = new ImageIcon(loader.getResource("themes/" + theme + "/one_winner.png"));
    	Square.circleWinBackground = new ImageIcon(loader.getResource("themes/" + theme + "/two_winner.png"));
    	
    	Mark.cross = new ImageIcon(loader.getResource("themes/" + theme + "/two.png"));
    	Mark.circle = new ImageIcon(loader.getResource("themes/" + theme + "/one.png"));
    	
    	board.repaint();
    }
    
    /* Sets isTwoPlayer  - whether or not the game is in two player mode */
    public void setNumberOfPlayers(int num) { numberOfPlayers = num; }
    
    /* Returns the game state  - game over or not */
    public boolean isGameOver() { return isGameOver; }
    
    /* Returns the AI for the game */
    public AI getAI() { return ai; }
    
    /* Returns the board */
    public Board getBoard() { return board; }
}