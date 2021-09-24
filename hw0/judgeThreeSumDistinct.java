/** Class that return true if there exist three integers in a that sum to zero and false otherwise.
 * The constraint here is that each number can be used only once.
 *  @author Weijie Yuan
 */
public class judgeThreeSumDistinct{
    public static boolean threeSumDistinct(int[] a){
        for (int i = 0; i < a.length; i += 1){
            for (int j = i + 1; j < a.length; j += 1){
                for (int k = j + 1; k < a.length; k += 1){
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
        System.out.println(threeSumDistinct(b));
    }
}