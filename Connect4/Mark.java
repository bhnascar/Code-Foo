import javax.swing.*;

/*The Mark class represents a game piece/mark, which is contained
 * by a Square (represented by the Square class).
 */
public class Mark
{
    private type mark;
    
    /* Instance field for actual mark images */
    public static ImageIcon circle, cross;
    
    /* Marks are either of type CIRCLE or type CROSS) */
    enum type {
        CIRCLE,
        CROSS,
    }
    
    /* Assigns a circle or cross to a new Mark */
    public Mark (type t) {
        mark = t;
    }
    
    /* Returns the appropriate symbol of the Mark */
    public ImageIcon getMark() {
        if (mark == Mark.type.CROSS) return cross;
        return circle;
    }
    
    /* Compares a Mark's mark to another Mark's mark */
    public boolean equals(Object other) { 
    	if (other instanceof Mark) return mark == ((Mark)other).getType(); 
    	return false;
    }
    
    /* Changes the mark images */
    public static void changeImage(String newCircle, String newCross) {
        circle = new ImageIcon(Controller.loader.getResource(newCircle));
        cross = new ImageIcon(Controller.loader.getResource(newCross));
    }
    
    /* Returns this Mark's mark type */
    public type getType() { return mark; }
}