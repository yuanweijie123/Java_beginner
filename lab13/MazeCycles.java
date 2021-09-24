import java.util.Stack;

/**
 *  @author Josh Hug
 */

public class MazeCycles extends MazeExplorer {
    /* Inherits protected fields:
    protected int[] distTo;
    protected int[] edgeTo;
    protected boolean[] marked;
    */
    private Stack<Integer> cycle;
    private int[] newEdgeTo;
    private Maze maze;

    /** Set up to find cycles of M. */
    public MazeCycles(Maze m) {
        super(m);
        maze = m;
    }

    @Override
    public void solve() {
        dfs(0);
    }

    private void dfs(int v) {
        cycle = new Stack<>();
        newEdgeTo = new int[maze.V()];
        marked[v] = true;
        announce();
        cycle.push(v);
        while (!cycle.isEmpty()) {
            int s = cycle.pop();
            marked[s] = true;
            announce();
            for (int w : maze.adj(s)) {
                if (!marked[w]) {
                    newEdgeTo[w] = s;
                    cycle.push(w);
                } else if (marked[w] && newEdgeTo[s] != w) {
                    newEdgeTo[w] = s;
                    int currStart = w;
                    int currDest = newEdgeTo[w];
                    edgeTo[currDest] = currStart;
                    currStart = currDest;
                    currDest = newEdgeTo[currStart];
                    while (currStart != w) {
                        edgeTo[currDest] = currStart;
                        currStart = currDest;
                        currDest = newEdgeTo[currStart];
                    }
                    announce();
                    return;
                }
            }
        }
    }
}

