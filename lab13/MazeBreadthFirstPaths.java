import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits visible fields:
    protected int[] distTo;
    protected int[] edgeTo;
    protected boolean[] marked;
    */

    /** Source. */
    private int s;
    /** Target. */
    private int t;

    /** A breadth-first search of paths in M from (SOURCEX, SOURCEY) to
     *  (TARGETX, TARGETY). */
    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY,
                                 int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs(int v) {
        // TODO: Your code here. Don't forget to update distTo, edgeTo,
        // and marked, as well as call announce()
        marked[v] = true;
        announce();
        if (v == t) {
            return;
        }
        Queue<Integer> q = new LinkedList<>();
        q.add(v);
        while (!q.isEmpty()) {
            int s = q.remove();
            for (int w : maze.adj(s)) {
                if (!marked[w]) {
                    edgeTo[w] = s;
                    distTo[w] = distTo[s] + 1;
                    marked[w] = true;
                    announce();
                    if (w == t) {
                        return;
                    }
                    q.add(w);
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
}

