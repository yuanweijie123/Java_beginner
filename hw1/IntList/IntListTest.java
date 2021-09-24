import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IntListTest {

    /** Sample test that verifies correctness of the IntList.list static
     *  method. The main point of this is to convince you that
     *  assertEquals knows how to handle IntLists just fine.
     */

    @Test
    public void testList() {
        IntList one = new IntList(1, null);
        IntList twoOne = new IntList(2, one);
        IntList threeTwoOne = new IntList(3, twoOne);

        IntList x = IntList.list(3, 2, 1);
        assertEquals(threeTwoOne, x);
    }

    /** Do not use the new keyword in your tests. You can create
     *  lists using the handy IntList.list method.
     *
     *  Make sure to include test cases involving lists of various sizes
     *  on both sides of the operation. That includes the empty list, which
     *  can be instantiated, for example, with
     *  IntList empty = IntList.list().
     *
     *  Keep in mind that dcatenate(A, B) is NOT required to leave A untouched.
     *  Anything can happen to A.
     */

    @Test
    public void testDcatenate() {
        IntList A1 = IntList.list();
        IntList B1 = IntList.list(1, 2, 3, 4);
        assertEquals(IntList.dcatenate(A1, B1), B1);

        IntList A2 = IntList.list(2, 5, 3);
        IntList B2 = IntList.list(6, 1, 10, 12);
        assertEquals(IntList.dcatenate(A2, B2), IntList.list(2, 5, 3, 6, 1, 10, 12));
    }

    /** Tests that subtail works properly. Again, don't use new.
     *
     *  Make sure to test that subtail does not modify the list.
     */

    @Test
    public void testSubtail() {
        IntList A = IntList.list(2, 1, 5, 10, 4, 7);
        IntList subtailA = IntList.subTail(A, 3);
        assertEquals(subtailA, IntList.list(10, 4, 7));
        assertEquals(A, IntList.list(2, 1, 5, 10, 4, 7));

        IntList B = IntList.list();
        IntList subtailB = IntList.subTail(B, 0);
        assertEquals(B, IntList.list());
    }

    /** Tests that sublist works properly. Again, don't use new.
     *
     *  Make sure to test that sublist does not modify the list.
     */

    @Test
    public void testSublist() {
        IntList A = IntList.list(2, 1, 5, 10, 4, 7);
        IntList sublistA = IntList.sublist(A, 3, 2);
        assertEquals(sublistA, IntList.list(10, 4));
        assertEquals(A, IntList.list(2, 1, 5, 10, 4, 7));

        IntList B = IntList.list();
        IntList sublistB = IntList.sublist(B, 0, -1);
        assertEquals(sublistB, null);
    }

    /** Tests that dSublist works properly. Again, don't use new.
     *
     *  As with testDcatenate, it is not safe to assume that list passed
     *  to dSublist is the same after any call to dSublist
     */

    @Test
    public void testDsublist() {
        IntList A = IntList.list(2, 1, 5, 10, 4, 7);
        IntList dsublistA = IntList.dsublist(A, 3, 2);
        assertEquals(dsublistA, IntList.list(10, 4));
        assertNotEquals(A, IntList.list(2, 1, 5, 10, 4, 7));

        IntList B = IntList.list();
        IntList dsublistB = IntList.dsublist(B, 0, -1);
        assertEquals(dsublistB, null);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(IntListTest.class));
    }
}
