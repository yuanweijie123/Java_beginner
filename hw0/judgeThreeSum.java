/** Class that return true if there exist three integers in a that sum to zero and false otherwise.
 * Integers may be used more than once.
 *  @author Weijie Yuan
 */
public class judgeThreeSum{
    public static boolean threeSum(int[] a){
        for (int i = 0; i < a.length; i += 1){
            for (int j = i; j < a.length; j += 1){
                for (int k = j; k < a.length; k += 1){
                    if (a[i] + a[j] + a[k] == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }
   public static void main(String[] args){
        int[] b = {5, 1, 0, 3, 6};
        System.out.println(threeSum(b));
    }
}