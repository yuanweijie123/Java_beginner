/** Class that find max value of the an int array.
 *  @author Weijie Yuan
 */
public class Maxint{
    static int max(int[] a){
      int max = a[0];
      for (int i = 1; i < a.length; i += 1){
        if (a[i] > max) {
          max = a[i];
        }
      }
      return max;
    }
    public static void main(String[] args){
        int[] b = {2, 5, 6, 11, -3, 4};
        System.out.println(max(b));
    }
}