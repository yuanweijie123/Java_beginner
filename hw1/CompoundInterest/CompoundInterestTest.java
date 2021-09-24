import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(1, CompoundInterest.numYears(2020));
        assertEquals(10, CompoundInterest.numYears(2029));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2021), tolerance);
        assertEquals(3.103, CompoundInterest.futureValue(20, -17, 2029), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(1.18026496E01, CompoundInterest.futureValueReal(10, 12, 2021, 3), tolerance);
        assertEquals(2.806486, CompoundInterest.futureValueReal(20, -17, 2029, 1), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2021, 10), tolerance);
        assertEquals(171.95, CompoundInterest.totalSavings(50, 2022, -10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(8109.5, CompoundInterest.totalSavingsReal(5000, 2021, 10, 30), tolerance);
        assertEquals(125.3516, CompoundInterest.totalSavingsReal(50, 2022, -10, 10), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
