import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** HW #7, Two-sum problem.
 * @author Weijie Yuan
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        Arrays.sort(A);
        Arrays.sort(B);
        int startA = 0;
        int endB = B.length - 1;
        while (startA < A.length && endB >= 0) {
            if (A[startA] + B[endB] == m) {
                return true;
            } else if (A[startA] + B[endB] < m) {
                startA ++;
            } else {
                endB --;
            }
        }
        return false;
    }

    @Test
    public void basicTest() {
        int[] A = new int[]{0, 1, 2, 3, 5};
        int[] B = new int[]{1, 3, 4, 8, 9};
        assertTrue(sumsTo(A, B, 7));
        assertFalse(sumsTo(A, B, 16));
    }
}
