import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.TreeSet;


/**
 *  A weighted graph.
 *  @author Weijie Yuan
 */
public class Graph {

    /** Adjacency lists by vertex number. */
    private LinkedList<Edge>[] adjLists;
    /** Number of vertices in me. */
    private int vertexCount;

    /** A graph with NUMVERTICES vertices and no edges. */
    @SuppressWarnings("unchecked")
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /** Add to the graph a directed edge from vertex V1 to vertex V2,
     *  with weight EDGEWEIGHT. If the edge already exists, replaces
     *  the weight of the current edge EDGEWEIGHT. */
    public void addEdge(int v1, int v2, int edgeWeight) {
        if (!isAdjacent(v1, v2)) {
            LinkedList<Edge> v1Neighbors = adjLists[v1];
            v1Neighbors.add(new Edge(v1, v2, edgeWeight));
        } else {
            LinkedList<Edge> v1Neighbors = adjLists[v1];
            for (Edge e : v1Neighbors) {
                if (e.to() == v2) {
                    e.edgeWeight = edgeWeight;
                }
            }
        }
    }

    /** Add to the graph an undirected edge from vertex V1 to vertex V2,
     *  with weight EDGEWEIGHT. If the edge already exists, replaces
     *  the weight of the current edge EDGEWEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int edgeWeight) {
        addEdge(v1, v2, edgeWeight);
        addEdge(v2, v1, edgeWeight);
    }

    /** Returns true iff there is an edge from vertex FROM to vertex TO. */
    public boolean isAdjacent(int from, int to) {
        for (Edge e : adjLists[from]) {
            if (e.to() == to) {
                return true;
            }
        }
        return false;
    }

    /** Returns a list of all the neighboring vertices u
     *  such that the edge (VERTEX, u) exists in this graph. */
    public List<Integer> neighbors(int vertex) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        for (Edge e : adjLists[vertex]) {
            neighbors.add(e.to());
        }
        return neighbors;
    }

    /** Runs Dijkstra's algorithm starting from vertex STARTVERTEX and returns
     *  an integer array consisting of the shortest distances
     *  from STARTVERTEX to all other vertices. */
    public int[] dijkstras(int startVertex) {
        // TODO: Your code here!
        boolean[] inSPT = new boolean[vertexCount];
        int [] distance = new int[vertexCount];

        //Initialize all the distances to infinity
        for (int i = 0; i < vertexCount ; i++) {
            distance[i] = Integer.MAX_VALUE;
        }

        TreeSet treeSet = new TreeSet<>(new nodeComparator());
        distance[startVertex] = 0;
        Node p0 = new Node(distance[startVertex],startVertex);
        //add it to tree set
        treeSet.add(p0);
        //while priority queue is not empty
        while(!treeSet.isEmpty()){
            //extract the min
            Node extractedNode = (Node) treeSet.pollFirst();

            //extracted vertex
            int extractedVertex = extractedNode.getVertex();
            if(!inSPT[extractedVertex]) {
                inSPT[extractedVertex] = true;

                //iterate through all the adjacent vertices and update the keys
                LinkedList<Edge> currAdj = adjLists[extractedVertex];
                for (int i = 0; i < currAdj.size(); i++) {
                    Edge edge = currAdj.get(i);
                    int destination = edge.to();
                    //only if edge destination is not present in mst
                    if (!inSPT[destination]) {
                        //check if distance needs an update or not
                        //means check total weight from source to vertex_V is less than
                        //the current distance value, if yes then update the distance
                        int newKey =  distance[extractedVertex] + edge.edgeWeight;
                        int currentKey = distance[destination];
                        Node p = new Node(currentKey, destination);
                        if(currentKey > newKey){
                            distance[destination] = newKey;
                            p.setDist(newKey);
                        }
                        treeSet.add(p);
                    }
                }
            }
        }
        //print Shortest Path Tree
        printDijkstra(distance, startVertex);
        return distance;
    }

    public void printDijkstra(int[] distance, int startVertex){
        System.out.println("Dijkstra Algorithm: (Adjacency List + TreeSet)");
        for (int i = 0; i < vertexCount; i++) {
            System.out.println("Start Vertex: " + startVertex + " to vertex " +   + i +
                    " distance: " + distance[i]);
        }
    }

    /** Returns the edge (V1, V2). (ou may find this helpful to implement!) */
    private Edge getEdge(int v1, int v2) {

        return null;
    }

    /** Represents an edge in this graph. */
    class Edge {

        /** End points of this edge. */
        private int from, to;
        /** Weight label of this edge. */
        int edgeWeight;

        /** The edge (V0, V1) with weight WEIGHT. */
        Edge(int v0, int v1, int weight) {
            this.from = v0;
            this.to = v1;
            this.edgeWeight = weight;
        }

        /** Return neighbor vertex along this edge. */
        public int to() {
            return to;
        }

        /** Return weight of this edge. */
        public int info() {
            return edgeWeight;
        }

        @Override
        public String toString() {
            return "(" + from + "," + to + ",dist=" + edgeWeight + ")";
        }

    }

    /** Tests of Graph. */
    public static void main(String[] unused) {
        // Put some tests here!

        Graph g1 = new Graph(5);
        g1.addEdge(0, 1, 1);
        g1.addEdge(0, 2, 1);
        g1.addEdge(0, 4, 1);
        g1.addEdge(1, 2, 1);
        g1.addEdge(2, 0, 1);
        g1.addEdge(2, 3, 1);
        g1.addEdge(4, 3, 1);
        g1.dijkstras(0);
        g1.dijkstras(1);
        g1.dijkstras(2);
        g1.dijkstras(3);
        g1.dijkstras(4);

        Graph g2 = new Graph(5);
        g2.addEdge(0, 1, 1);
        g2.addEdge(0, 2, 1);
        g2.addEdge(0, 4, 1);
        g2.addEdge(1, 2, 1);
        g2.addEdge(2, 3, 1);
        g2.addEdge(4, 3, 1);
        g2.dijkstras(0);
        g2.dijkstras(1);
        g2.dijkstras(2);
        g2.dijkstras(3);
        g2.dijkstras(4);
    }
}

class Node {

    // instance member variables
    private int distance;
    private int vertex;

    // parameterized constructor
    Node(Integer distance, Integer vertex) {
        this.distance = distance;
        this.vertex = vertex;
    }

    Integer getDist() {
        return distance;
    }

    public void setDist(Integer distance) {
        this.distance = distance;
    }

    Integer getVertex() {
        return vertex;
    }

    public void setVertex(Integer vertex) {
        this.vertex = vertex;
    }
}

class nodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node n1, Node n2) {
        //sort using distance values
        int key1 = n1.getDist();
        int key2 = n2.getDist();
        if (key1 > key2) {
            return 1;
        } else if (key1 < key2) {
            return -1;
        } else {
            return (n1.getVertex() - n2.getVertex());
        }
    }
}
