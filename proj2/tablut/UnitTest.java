package tablut;

import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** The suite of all JUnit tests for the enigma package.
 *  @author Weijie Yuan
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** A dummy test as a placeholder for real ones. */
    @Test
    public void isBlockedMoveTest() {
        Board newBoard = new Board();
        newBoard.makeMove(Square.sq(3, 8), Square.sq(3, 6));
        assertFalse(newBoard.isUnblockedMove(Square.sq(3, 4), Square.sq(3, 7)));
        assertTrue(newBoard.isUnblockedMove(Square.sq(3, 4), Square.sq(3, 5)));
    }

    @Test
    public void kingPosTest() {
        Board newBoard = new Board();
        assertEquals(newBoard.kingPosition(), Square.sq(4, 4));
        newBoard.makeMove(Square.sq(3, 0), Square.sq(1, 0));
        assertEquals(newBoard.kingPosition(), Square.sq(4, 4));
        newBoard.makeMove(Square.sq(3, 4), Square.sq(3, 7));
        assertEquals(newBoard.kingPosition(), Square.sq(4, 4));
        newBoard.makeMove(Square.sq(5, 8), Square.sq(8, 8));
        assertEquals(newBoard.kingPosition(), Square.sq(4, 4));
        newBoard.makeMove(Square.sq(4, 4), Square.sq(3, 4));
        assertEquals(newBoard.kingPosition(), Square.sq(3, 4));
    }

    @Test
    public void isLegalMoveTest() {
        Board newBoard = new Board();
        assertTrue(newBoard.isLegal(Square.sq(0, 3)));
        assertFalse(newBoard.isLegal(Square.sq(4, 2)));

        Board newBoard1 = new Board();
        newBoard1.makeMove(Square.sq(5, 8), Square.sq(8, 8));
        assertTrue(newBoard1.isLegal(Square.sq(4, 4)));
        assertFalse(newBoard1.isUnblockedMove(Square.sq(4, 4),
                Square.sq(3, 4)));
        assertFalse(newBoard1.isLegal(Square.sq(4, 4), Square.sq(3, 4)));
    }

    @Test
    public void captureTest() {
        Board newBoard = new Board();
        newBoard.makeMove(Square.sq(5, 8), Square.sq(5, 6));
        newBoard.makeMove(Square.sq(4, 3), Square.sq(6, 3));
        newBoard.makeMove(Square.sq(3, 8), Square.sq(3, 6));
        assertSame(newBoard.get(Square.sq(4, 6)), Piece.EMPTY);
    }

    @Test
    public void undoTest() {
        Board newBoard = new Board();
        newBoard.makeMove(Square.sq(5, 8), Square.sq(5, 6));
        newBoard.undo();
        assertSame(newBoard.get(Square.sq(5, 6)), Piece.EMPTY);
        assertSame(newBoard.get(Square.sq(5, 8)), Piece.BLACK);
        newBoard.makeMove(Square.sq(5, 8), Square.sq(5, 6));
        newBoard.makeMove(Square.sq(4, 3), Square.sq(6, 3));
        newBoard.makeMove(Square.sq(3, 8), Square.sq(3, 6));
        newBoard.undo();
        assertSame(newBoard.get(Square.sq(4, 6)), Piece.WHITE);
        assertSame(newBoard.get(Square.sq(3, 8)), Piece.BLACK);
        assertSame(newBoard.get(Square.sq(3, 6)), Piece.EMPTY);
    }

    @Test
    public void repeatTest() {
        Board newBoard = new Board();
        newBoard.makeMove(Square.sq(5, 8), Square.sq(5, 6));
        newBoard.makeMove(Square.sq(4, 3), Square.sq(6, 3));
        newBoard.makeMove(Square.sq(5, 6), Square.sq(5, 8));
        newBoard.makeMove(Square.sq(6, 3), Square.sq(4, 3));
        assertTrue(newBoard.repeatedPosition());
        assertSame(newBoard.winner(), Piece.BLACK);
    }

    @Test
    public void winnerTest() {
        Board newBoard = new Board();
        newBoard.makeMove(Move.mv("a4-2"));
        newBoard.makeMove(Move.mv("e4-h"));
        newBoard.makeMove(Move.mv("i6-9"));
        newBoard.makeMove(Move.mv("e5-4"));
        newBoard.makeMove(Move.mv("i9-h"));
        newBoard.makeMove(Move.mv("e4-a"));
        System.out.println(newBoard.winner());
    }
}


