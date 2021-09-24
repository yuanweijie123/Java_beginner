import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** HW #7, Count inversions.
 *  @author Weijie Yuan
 */
public class Inversions {

    /** A main program for testing purposes.  Prints the number of inversions
     *  in the sequence ARGS. */
    public static void main(String[] args) {
        System.out.println(inversions(Arrays.asList(args)));
    }

    /** Return the number of inversions of T objects in ARGS. */
    public static <T extends Comparable<? super T>> int inversions(List<T> args) {
        List<T> temp = new ArrayList<T>(args.size());
        for (int i = 0; i < args.size(); i++) {
            temp.add(null);
        }
        return mergeCount(args, temp, 0, args.size() - 1);
    }

    public static <T extends Comparable<? super T>> int mergeCount(List<T> arr, List<T> temp, int left, int right) {
        int mid, invCount = 0;
        if (right > left) {
            mid = (right + left) / 2;
            invCount = mergeCount(arr, temp, left, mid);
            invCount += mergeCount(arr, temp, mid + 1, right);
            invCount += merge(arr, temp, left, mid + 1, right);
        }
        return invCount;
    }

    public static <T extends Comparable<? super T>> int merge(List<T> arr, List<T> temp, int left, int mid, int right) {
        int i, j, k;
        int inv_count = 0;

        i = left;
        j = mid;
        k = left;
        while ((i <= mid - 1) && (j <= right)) {
            if (arr.get(i).compareTo(arr.get(j)) <= 0) {
                temp.set(k++, arr.get(i++));
            }
            else {
                temp.set(k++, arr.get(j++));
                inv_count = inv_count + (mid - i);
            }
        }
        while (i <= mid - 1) {
            temp.set(k++, arr.get(i++));
        }
        while (j <= right){
            temp.set(k++, arr.get(j++));
        }
        for (i = left; i <= right; i++) {
            arr.set(i, temp.get(i));
        }

        return inv_count;
    }

}
