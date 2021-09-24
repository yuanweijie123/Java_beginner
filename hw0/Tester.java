import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Tests for hw0. 
 *  @author YOUR NAMES HERE
 */
public class Tester {

    /* Feel free to add your own tests.  For now, you can just follow
     * the pattern you see here.  We'll look into the details of JUnit
     * testing later.
     *
     * To actually run the tests, just use
     *      java Tester 
     * (after first compiling your files).
     *
     * DON'T put your HW0 solutions here!  Put them in a separate
     * class and figure out how to call them from here.  You'll have
     * to modify the calls to max, threeSum, and threeSumDistinct to
     * get them to work, but it's all good practice! */

    @Test
    public void maxTest() {
        // Change call to max to make this call yours.
        assertEquals(14, Maxint.max(new int[] { 0, -5, 2, 14, 10 }));
        assertEquals(5, Maxint.max(new int[] { 1, 2, 3, 4, 5 }));
        assertEquals(11, Maxint.max(new int[] { 1, 11, 2, 3, 4, 9 }));
        assertEquals(-2, Maxint.max(new int[] { -30, -20, -10, -9, -2, -11}));
        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void threeSumTest() {
        // Change call to threeSum to make this call yours.
        assertTrue(judgeThreeSum.threeSum(new int[] { -6, 2, 4 }));
        assertFalse(judgeThreeSum.threeSum(new int[] { -6, 2, 5 }));
        assertTrue(judgeThreeSum.threeSum(new int[] { -6, 3, 10, 200 }));
        assertTrue(judgeThreeSum.threeSum(new int[] { 8, 2, -1, 15 }));
        assertTrue(judgeThreeSum.threeSum(new int[] { 8, 2, -1, -1, 15 }));
        assertTrue(judgeThreeSum.threeSum(new int[] { 5, 1, 0, 3, 6 }));
        // REPLACE THIS WITH MORE TESTS.
    }

    @Test
    public void threeSumDistinctTest() {
        // Change call to threeSumDistinct to make this call yours.
        assertTrue(judgeThreeSumDistinct.threeSumDistinct(new int[] { -6, 2, 4 }));
        assertFalse(judgeThreeSumDistinct.threeSumDistinct(new int[] { -6, 2, 5 }));
        assertFalse(judgeThreeSumDistinct.threeSumDistinct(new int[] { -6, 3, 10, 200 }));
        assertFalse(judgeThreeSumDistinct.threeSumDistinct(new int[] { 8, 2, -1, 15 }));
        assertTrue(judgeThreeSumDistinct.threeSumDistinct(new int[] { 8, 2, -1, -1, 15 }));
        assertFalse(judgeThreeSumDistinct.threeSumDistinct(new int[] { 5, 1, 0, 3, 6 }));
        // REPLACE THIS WITH MORE TESTS.
    }

    public static void main(String[] unused) {
        textui.runClasses(Tester.class);
    }

}
