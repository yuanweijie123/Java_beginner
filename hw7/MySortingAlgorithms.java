import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Distribution Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i++) {
                for (int j = i; j > 0 && array[j] < array[j - 1]; j--) {
                    swap(array, j, j-1);
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i++) {
                int minIndex = i;
                for (int j = i + 1; j < k; j++) {
                    if (array[j] < array[minIndex]) {
                        minIndex = j;
                    }
                }
                swap(array, i, minIndex);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int[] workArray = new int[array.length];
            int upper = Math.min(k, array.length);
            SplitMerge(array, 0, upper, workArray);
        }

        void SplitMerge(int[] array, int start, int end, int[] workArray) {
            if (end - start < 2) {
                return;
            }
            int middle = (end + start) / 2;
            SplitMerge(array, start, middle, workArray);
            SplitMerge(array, middle, end, workArray);
            Merge(workArray, start, middle, end, array);
        }

        void Merge(int[] workArray, int start, int middle, int end, int[] array) {
            int i = start;
            int j = middle;
            System.arraycopy(array, start, workArray, start, end - start);
            for (int k = start; k < end; k++) {
                if (i < middle && (j >= end || workArray[i] <= workArray[j])) {
                    array[k] = workArray[i];
                    i += 1;
                } else {
                    array[k] = workArray[j];
                    j += 1;
                }
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int maximum = Integer.MIN_VALUE;
            for (int i = 0; i < k; i++) {
                if (array[i] > maximum) {
                    maximum = array[i];
                }
            }

            int[] count = new int[maximum + 1];
            for (int i = 0; i < k; i++) {
                count[array[i]] += 1;
            }

            int[] starts = new int[maximum + 1];
            starts[0] = 0;
            for (int i = 1; i <= maximum; i++) {
                starts[i] = starts[i-1] + count[i-1];
            }

            int[] workArray = new int[k];
            for (int i = 0; i < k; i++) {
                workArray[starts[array[i]]] = array[i];
                starts[array[i]] += 1;
            }

            System.arraycopy(workArray, 0, array, 0, k);
        }

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = k / 2 - 1; i >= 0; i--)
                maxHeapify(array, i, k);
            for (int i = k - 1; i >= 0; i--) {
                swap(array, 0, i);
                maxHeapify(array, 0, i);
            }
        }

        void maxHeapify(int[] arr, int i, int n) {
            int smallIndex = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < n && arr[left] > arr[smallIndex])
                smallIndex = left;
            if (right < n && arr[right] > arr[smallIndex])
                smallIndex = right;
            if (smallIndex != i) {
                swap(arr, i, smallIndex);
                maxHeapify(arr, smallIndex, n);
            }
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            quicksort(array, 0, k - 1);
        }

        private static void quicksort(int[] a, int lo, int hi) {
            if (hi <= lo) {
                return;
            }
            int j = partition(a, lo, hi);
            quicksort(a, lo, j - 1);
            quicksort(a, j + 1, hi);
        }

        private static int partition(int[] a, int lo, int hi) {
            List<Integer> smaller = new ArrayList<Integer>();
            List<Integer> equal = new ArrayList<Integer>();
            List<Integer> larger = new ArrayList<Integer>();

            int pivot = a[lo];
            for (int i = lo; i <= hi; i += 1) {
                if (a[i] < pivot) {
                    smaller.add(a[i]);
                } else if (a[i] > pivot) {
                    larger.add(a[i]);
                } else {
                    equal.add(a[i]);
                }
            }

            List<Integer> partitioned = new ArrayList<Integer>();

            partitioned.addAll(smaller);
            partitioned.addAll(equal);
            partitioned.addAll(larger);

            int[] partitionedArray = convertListToArray(partitioned);
            System.arraycopy(partitionedArray, 0, a, lo, partitionedArray.length);

            return smaller.size() + lo;
        }

        private static int[] convertListToArray(List<Integer> al) {
            int[] returnArray = new int[al.size()];

            for (int i = 0; i < al.size(); i += 1) {
                returnArray[i] = al.get(i);
            }

            return returnArray;
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     * https://algs4.cs.princeton.edu/51radix/LSD.java.html
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            final int BITS_PER_BYTE = 8;
            final int BITS = 32;
            final int R = 1 << BITS_PER_BYTE;
            final int MASK = R - 1;
            final int w = BITS / BITS_PER_BYTE;

            int[] aux = new int[k];

            for (int d = 0; d < w; d++) {
                int[] count = new int[R+1];
                for (int i = 0; i < k; i++) {
                    int c = (a[i] >> BITS_PER_BYTE*d) & MASK;
                    count[c + 1]++;
                }

                for (int r = 0; r < R; r++)
                    count[r+1] += count[r];

                if (d == w-1) {
                    int shift1 = count[R] - count[R/2];
                    int shift2 = count[R/2];
                    for (int r = 0; r < R/2; r++)
                        count[r] += shift1;
                    for (int r = R/2; r < R; r++)
                        count[r] -= shift2;
                }

                for (int i = 0; i < k; i++) {
                    int c = (a[i] >> BITS_PER_BYTE*d) & MASK;
                    aux[count[c]++] = a[i];
                }

                for (int i = 0; i < k; i++)
                    a[i] = aux[i];
            }
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
