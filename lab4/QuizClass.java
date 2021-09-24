import java.awt.Point;

public class QuizClass {
    /**
     * A pair of points
     */
    Point[] ref;
    public static void main(String[] args) {
        // Put your statements here.
        QuizClass qobj;
        qobj = new QuizClass();
        qobj.ref = new Point[2];
        int x = 3;
        int y = 7;
        qobj.ref[0] = new Point(x, y);
        qobj.ref[1] = new Point(x, y);
    }
}