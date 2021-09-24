package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Weijie Yuan
 */
class Arrays {
    /* C. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        if (A == null) {
            return B;
        }
        if (B == null) {
            return A;
        }
        int[] C = new int[A.length + B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);
        return C;
    }

    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        int[] r = new int[A.length - len];
        if (start + len >= A.length) {
            System.arraycopy(A, 0, r, 0, A.length - len);
        }
        else {
            System.arraycopy(A, 0, r, 0, start);
            System.arraycopy(A, start + len, r, start, A.length - len - start);
        }
        return r;
    }

    /* E. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        if (A.length == 0) return new int[0][];
        int num_list = 1;
        for (int i = 0; i < A.length - 1; i += 1){
            if (A[i] >= A[i + 1]) num_list += 1;
        }
        int[][] result = new int[num_list][];
        num_list = 0;
        int start = 0;
        for (int i = 0; i < A.length - 1; i += 1) {
            if (A[i] >= A[i + 1]){
                result[num_list] = Utils.subarray(A, start, i + 1 - start);
                start = i + 1;
                num_list += 1;
            }
        }
        result[num_list] = Utils.subarray(A, start, A.length - start);
        return result;
    }
}
