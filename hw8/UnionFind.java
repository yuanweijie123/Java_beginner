import java.util.Arrays;

/** A partition of a set of contiguous integers that allows (a) finding whether
 *  two integers are in the same partition set and (b) replacing two partitions
 *  with their union.  At any given time, for a structure partitioning
 *  the integers 1-N, each partition is represented by a unique member of that
 *  partition, called its representative.
 *  @author Weijie Yuan
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        parent = new int[N + 1];
        size = new int[N + 1];
        for (int i = 1; i < N + 1; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    /** Return the representative of the partition currently containing V.
     *  Assumes V is contained in one of the partitions.  */
    public int find(int v) {
        while (v != parent[v]) {
            parent[v] = parent[parent[v]]; // implements path compression
            v = parent[v];
        }
        return v;
    }

    /** Return true iff U and V are in the same partition. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single partition, returning its representative. */
    public int union(int u, int v) {
        int i = find(u);
        int j = find(v);
        if (i == j) {
            return i;
        }

        if (size[i] < size[j]) {
            parent[i] = j;
            size[j] += size[i];
            return j;
        } else {
            parent[j] = i;
            size[i] += size[j];
            return i;
        }
    }

    private int[] parent; // id[i] = representative of i
    private int[] size; // size[i] = number of ints in subtree rooted at i
}
