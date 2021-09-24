package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** Test 'naturalRuns' on IntListLsit works fine
 *  @author Weijie Yuan
 */

public class ListsTest {
    @Test
    public void testLists() {
        /** Test 1 */
        IntList L = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        IntListList res1 = IntListList.list(new int[][]{{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}});

        IntListList x1 = Lists.naturalRuns(L);
        assertEquals(res1, x1);

        /** Test 2 */
        IntList M = IntList.list(10, 9, 8, 7, 6);
        IntListList res2 = IntListList.list(new int[][]{{10}, {9}, {8}, {7}, {6}});

        IntListList x2 = Lists.naturalRuns(M);
        assertEquals(res2, x2);

        /** Test 3 */
        IntList N = IntList.list(1, 2, 3, 4);
        IntListList res3 = IntListList.list(new int[][]{{1, 2, 3, 4}});

        IntListList x3 = Lists.naturalRuns(N);
        assertEquals(res3, x3);

        /** Test 4 */
        IntList K = IntList.list();

        IntListList x4 = Lists.naturalRuns(K);
        assertEquals(null, x4);

        /** Test 5 */
        IntList J = IntList.list(1);
        IntListList res5 = IntListList.list(new int[][]{{1}});;

        IntListList x5 = Lists.naturalRuns(J);
        assertEquals(res5, x5);
    }

    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
