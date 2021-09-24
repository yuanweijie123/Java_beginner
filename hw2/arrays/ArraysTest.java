package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** Sample test that verifies correctness of the Arrays static
 *  method. The main point here is using Utils.equals to confirm
 *  that methods in Arrays class work fine.
 *  @author Weijie Yuan
 */

public class ArraysTest {
    /** Test array functions work fine.
     */
    @Test
    public void testCatenate() {
        int[] A1 = {1, 3, 2};
        int[] B1 = {4, 6};
        int[] res1 = {1, 3, 2, 4, 6};
        int[] C1 = Arrays.catenate(A1, B1);
        assertArrayEquals(res1, C1);

        int[] A2 = null;
        int[] B2 = {1, 2, 4};
        int[] C2 = Arrays.catenate(A2, B2);
        assertArrayEquals(B2, C2);

        int[] A3 = null;
        int[] B3 = null;
        int[] C3 = Arrays.catenate(A3, B3);
        assertArrayEquals(B3, C3);
    }

    @Test
    public void testRemove() {
        int[] A1 = {1, 2, 3, 5, 7, 8};
        int[] ex1 = {7, 8};
        int[] r1 = Arrays.remove(A1, 0, 4);
        assertArrayEquals(ex1, r1);

        int[] A2 = {4, 3, 5, 8, 1};
        int[] ex2 = {4, 3, 1};
        int[] r2 = Arrays.remove(A2, 2, 2);
        assertArrayEquals(ex2, r2);

        int[] A3 = {1, 3, 2};
        int[] ex3 = {1};
        int[] r3 = Arrays.remove(A3, 2, 2);
        assertArrayEquals(ex3, r3);
    }

    @Test
    public void testNaturalRuns() {
        int[] A1 = {1, 3, 7, 5, 4, 6, 9, 10};
        int[][] result1 = {{1, 3, 7}, {5}, {4, 6, 9, 10}};
        assertArrayEquals(result1, Arrays.naturalRuns(A1));

        int[] A2 = {5, 3, 1, -1};
        int[][] result2 = {{5}, {3}, {1}, {-1}};
        assertArrayEquals(result2, Arrays.naturalRuns(A2));

        int[] A4 = {1, 2, 5, 5, 7, 9};
        int[][] result4 = {{1, 2, 5}, {5, 7, 9}};
        assertArrayEquals(result4, Arrays.naturalRuns(A4));

        int[] A5 = {1};
        int[][] result5 = new int[][] {{1}};
        assertArrayEquals(result5, Arrays.naturalRuns(A5));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
