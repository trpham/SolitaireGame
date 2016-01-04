package canfield;

import static org.junit.Assert.*;
import org.junit.Test;

/** Tests of the Game class.
 *  @author Truong Pham
 */

public class GameTest {

    /** Test intial score of the game */
    @Test
    public void testInitialScore() {
        Game g1 = new Game();
        g1.deal();
        assertEquals(5, g1.getScore());
    }

    /** Test undo method with some simply stockToWaste calls. */
    @Test
    public void testUndo() {
        Game g = new Game();
        g.deal();
        g.stockToWaste();
        Card A = g.topWaste();
        g.stockToWaste();
        g.stockToWaste();
        g.stockToWaste();
        g.undo();
        g.undo();
        g.undo();
        Card B = g.topWaste();
        assertEquals(A, B);
    }

    /** Test undo method with seed. */
    @Test
    public void testUndo2() {
        Game g = new Game();
        g.seed(10);
        g.deal();

        Card threeS = g.getTableau(3, 0);
        g.tableauToFoundation(3);
        g.undo();

        assertEquals(threeS, g.getTableau(3, 0));

        g.stockToWaste();
        g.stockToWaste();

        Card twoS = g.topWaste();
        g.wasteToTableau(4);
        g.undo();

        assertEquals(twoS, g.topWaste());

        Card fourC = g.getTableau(1, 0);
        g.wasteToTableau(4);
        g.tableauToTableau(4, 1);
        g.undo();
        assertEquals(fourC, g.getTableau(1, 0));
    }
}
