import utils.Filter;
import utils.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

/** Exercises for Lab 5.
 *  @author Weijie Yuan
 */
public class FilterClient {

    /** A couple of test cases. */
    private static final Integer[][] TESTS = {
        { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
        { 1, 2, 3, 0, 7, 8, 6, 9, 10, 1 }
    };

    /** Print out the items returned by L. */
    static void printAll(Filter<Integer> L) {
        System.out.print("[");
        String sep;
        sep = "";
        for (Integer i : L) {
            System.out.print(sep + i);
            sep = ", ";
        }
        System.out.println("]");
    }

    /** A sample space where you can experiment with your filter.
      * ARGS is unused. */
    public static void main(String[] args) {
        for (Integer[] data: TESTS) {
            List<Integer> L = Arrays.asList(data);
            System.out.println(L);
            Filter<Integer> f1 = new TrivialFilter<Integer>(L.iterator());
            Filter<Integer> f2 = everyFourth(L.iterator());
            Filter<Integer> f3 = evenNumberFilter(L.iterator());
            printAll(f1);
            printAll(f2);
            printAll(f3);
        }
    }

    /* Extra Challenges that you should complete without creating
       any new Filter implementations (i.e. you can create them
       using Trivial, Alternating, Monotonic, and/or PredicateFilter)
       1. Create a filter everyFourth that prints every fourth
       item.
       2. Create a filter that prints only even valued items. You
       may find the Even class provided below to be helpful. */

    /** Returns a filter that delivers every fourth item of INPUT,
     *  starting with the first.  You should not need to define a new
     *  class. */
    static Filter<Integer> everyFourth(Iterator<Integer> input) {

        /** A filter of values from INPUT that lets through every other
         *  value. */
        Filter<Integer> everyOther = new AlternatingFilter<>(input);
        return new AlternatingFilter<>(everyOther);
    }

    /** Returns a filter that delivers every even valued integer of
     *  INPUT. You should not need to define a new class. */
    static Filter<Integer> evenNumberFilter(Iterator<Integer> input) {
        Filter<Integer> evenElement = new PredicateFilter<>(new Even(), input);
        return evenElement;
    }

    /** A class whose instances represent the test for evenness. */
    static class Even implements Predicate<Integer> {
        @Override
        public boolean test(Integer x) {
            if (x % 2 == 0) {
                return true;
            } else {
                return false;
            }
        }
    }
}
