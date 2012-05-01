import java.util.*;

/*The AI class represents a computer opponent for a single-player game.*/
public class AI {
    
    private Controller controller;

    /*Creates a new AI opponent*/
    public AI (Controller c) {
        controller = c;
    }
    
    /*AI selects a move*/
    public Square move(Square s) {
    	ArrayList<Square> availableSpots = retrieveEmptySquares();
    	Square play = availableSpots.get((int)(Math.random() * availableSpots.size()));
    	int level = 0;
    	for(Square square : availableSpots) {
			square.setMark(new Mark(Mark.type.CROSS));
			int num = controller.check(square);
			
			// If this is a winning move, we can pick it right away
			square.setMark(new Mark(Mark.type.CIRCLE));
			int num2 = controller.check(square);
			if (num2 > 3) return square;
			
			int x = square.getBoardX();
			int y = square.getBoardY() - 1;
			Square s2 = controller.getBoard().getSquareAt(x, y);
			int num3 = 0;
			if (s2 != null && s2.getMark() == null) {
				s2.setMark(new Mark(Mark.type.CROSS));
				num3 = controller.check(s2);
				s2.setMark(null);
			}
			
			// Pick the best blocking move that does not enable 
			// a winning move by the player on the next turn
			if (num > level && num3 < 4) {
				play = square;
				level = num;
    		}
			
			square.setMark(null);
    	}
    	return play;
    }
    
    /*Returns an ArrayList of all empty Squares on the board*/
    public ArrayList<Square> retrieveEmptySquares() {
        Square[][] squares = controller.getBoard().getSquares();
        ArrayList<Square> emptySquares = new ArrayList<Square>();
        for (int x = 0; x < squares[0].length; x++) {
        	for (int y = squares.length - 1; y >= 0; y--) {
        		Square s = squares[y][x];
        		if (s.getMark() == null) {
        			emptySquares.add(s);
        			break;
        		}
        	}
        }
        return emptySquares;
    }
}