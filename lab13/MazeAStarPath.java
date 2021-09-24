import java.util.LinkedList;
import java.util.TreeSet;

/**
 *  @author Josh Hug
 */

public class MazeAStarPath extends MazeExplorer {
    /** Source. */
    private int s;
    /** Target. */
    private int t;
    /** True iff target has been processed. */
    private boolean targetFound = false;

    /** Set up to find path through M from (SOURCEX, SOURCEY) to
     *  (TARGETX, TARGETY) via A* search. */
    public MazeAStarPath(Maze m, int sourceX,
                         int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Returns estimate of the distance from V to the target. */
    private int h(int v) {
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Returns vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        /* You do not have to use this method. */
        return -1;
    }

    /** Performs an A star search from vertex START. */
    private void astar(int start) {
        int[] fScore = new int[maze.V()];

        for (int i = 0; i < maze.V() ; i++) {
            fScore[i] = Integer.MAX_VALUE;
        }

        TreeSet treeSet = new TreeSet<>(new nodeComparator());
        fScore[start] = h(start);
        Node p0 = new Node(fScore[start], start);
        treeSet.add(p0);
        while(!treeSet.isEmpty()){
            //extract the min
            Node extractedNode = (Node) treeSet.pollFirst();
            int extractedVertex = extractedNode.getVertex();
            if(!marked[extractedVertex]) {
                marked[extractedVertex] = true;
                announce();
                if (extractedVertex == t) {
                    return;
                }
                //iterate through all the adjacent vertices and update the keys
                for (int neigh : maze.adj(extractedVertex)) {
                    if (!marked[neigh]) {
                        //check if distance needs an update or not
                        //means check total weight from source to vertex_V is less than
                        //the current distance value, if yes then update the distance
                        distTo[neigh] = distTo[extractedVertex] + 1;
                        int newKey =  distTo[extractedVertex] + 1 + h(neigh);
                        int currentKey = fScore[neigh];
                        Node p = new Node(currentKey, neigh);
                        if(currentKey > newKey){
                            fScore[neigh] = newKey;
                            p.setDist(newKey);
                            edgeTo[neigh] = extractedVertex;
                            treeSet.add(p);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

