import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

/** Minimal spanning tree utility.
 *  @author
 */
public class MST {

    /** Given an undirected, weighted, connected graph whose vertices are
     *  numbered 1 to V, and an array E of edges, returns an array of edges
     *  in E that form a minimal spanning tree of the input graph.
     *  Each edge in E is a three-element int array of the form (u, v, w),
     *  where 0 < u < v <= V are vertex numbers, and 0 <= w is the weight
     *  of the edge. The result is an array containing edges from E.
     *  Neither E nor the arrays in it may be modified.  There may be
     *  multiple edges between vertices.  The objects in the returned array
     *  are a subset of those in E (they do not include copies of the
     *  original edges, just the original edges themselves.) */
    public static int[][] mst(int V, int[][] E) {
        Integer[] parent = new Integer[V + 1];
        Integer[] dist = new Integer[V + 1];
        Boolean[] mstset = new Boolean[V + 1];
        ArrayList<ArrayList<int[]>> adj = new ArrayList<>();
        TreeSet<int[]> MST = new TreeSet<>(SAME_EDGES);

        for (int vertex = 1; vertex <= V; vertex++) {
            dist[vertex] = Integer.MAX_VALUE;
            mstset[vertex] = false;
            parent[vertex] = 0;
            adj.add(new ArrayList<>());
        }
        mstset[0] = false;
        adj.add(new ArrayList<>());

        for (int[] e: E) {
            adj.get(e[0]).add(e);
            adj.get(e[1]).add(e);
        }

        dist[1] = 0;
        mstset[1] = true;
        parent[1] = 1;
        TreeSet<int[]> fringe = new TreeSet<>(DIST_VERTEX_COMPARATOR);
        for (int vertex = 1; vertex <= V; vertex++) {
            fringe.add(new int[]{vertex, dist[vertex]});
        }

        while (!fringe.isEmpty()) {
            int[] v = fringe.pollFirst();
            int s = v[0];
            mstset[s] = true;

            for (int[] e: adj.get(s)) {
                int d;
                if (e[0] == s) {
                    d = e[1];
                } else {
                    d = e[0];
                }

                if (!mstset[d] && e[2] < dist[d]) {
                    dist[d] = e[2];
                    fringe.add(new int[]{d, e[2]});
                    parent[d] = s;
//                    MST.add(e);
                }
            }

        }
        for (int[] e : E) {
            if (parent[e[0]] == e[1] || parent[e[1]] == e[0]) {
                MST.add(e);
            }
        }

        return MST.toArray(new int[MST.size()][]);
    }

    /** An ordering of edges by weight. */
    private static final Comparator<int[]> EDGE_WEIGHT_COMPARATOR =
        new Comparator<int[]>() {
            @Override
            public int compare(int[] e0, int[] e1) {
                return e0[2] - e1[2];
            }
        };

    private static final Comparator<int[]> DIST_VERTEX_COMPARATOR =
        new Comparator<int[]>() {
            @Override
            public int compare(int[] v0, int[] v1) {
                if (v0[1] - v1[1] != 0) {
                    return v0[1] - v1[1];
                } else {
                    return v0[0] - v1[0];
                }
            }
        };

    private static final Comparator<int[]> SAME_EDGES =
            new Comparator<int[]>() {
                @Override
                public int compare(int[] e0, int[] e1) {
                    int c = e0[0] - e1[0];
                    if (c != 0) {
                        return c;
                    } else {
                        return e0[1] - e1[1];
                    }
                }
            };
}
