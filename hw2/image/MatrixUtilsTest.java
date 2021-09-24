package image;

import arrays.Utils;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/** FIXME
 *  @author Weijie Yuan
 */

public class MatrixUtilsTest {
    /** FIXME
     */
    @Test
    public void testAccumulateVertical() {
        double[][] m = new double[][] {
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515, 38273, 1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}
        };
        double[][] c = MatrixUtils.accumulateVertical(m);
        double[][] expect1 = new double[][] {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };
        assertArrayEquals(c, expect1);

        double[][] n = new double[][] {{10, 4, 5, 6}, {3, 10, 18, 6}, {8, 5, 19, 6}};
        double[][] b = MatrixUtils.accumulateVertical(n);
        double[][] expect2 = new double[][] {
                {10, 4, 5, 6}, {7, 14, 22, 11}, {15, 12, 30, 17}
        };
        assertArrayEquals(b, expect2);
    }

    @Test
    public void testTranspose() {
        double[][] m = new double[][] {{1, 2}, {3, 4}};
        double[][] mT = new double[][] {{1,3}, {2, 4}};
        assertArrayEquals(mT, MatrixUtils.transpose(m));
    }

    @Test
    public void testAccumulate() {
        double[][] m = new double[][] {
                {1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515, 38273, 1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}
        };
        double[][] expect = new double[][] {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };
        double[][] mT = MatrixUtils.transpose(m);
        assertArrayEquals(MatrixUtils.accumulate(mT, MatrixUtils.Orientation.HORIZONTAL),
                MatrixUtils.transpose(expect));
    }

    @Test
    public void testFindMinIndex() {
        double[] l = new double[] {2089520, 1162923, 1124919, 2098278};
        int minIndex = MatrixUtils.findIndexMin(l, 0, 2);
        assertEquals(2, minIndex);
    }

    @Test
    public void testFindVerticalSeam() {
        double[][] m = new double[][] {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };
        assertArrayEquals(new int[] {1, 2, 1, 1, 2, 1}, MatrixUtils.findVerticalSeam(m));
    }

    @Test
    public void testFindSea() {
        double[][] m = new double[][] {
                {1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}
        };
        assertArrayEquals(new int[] {1, 2, 1, 1, 2, 1},
                MatrixUtils.findSeam(MatrixUtils.transpose(m), MatrixUtils.Orientation.HORIZONTAL));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
