package sample;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Graph. Could be directed or undirected depending on the TYPE enum. A graph is
 * an abstract representation of a set of objects where some pairs of the
 * objects are connected by links.
 * <p>
 * @see <a href="https://en.wikipedia.org/wiki/Graph_(mathematics)">Graph (Wikipedia)</a>
 * <br>
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
@SuppressWarnings("unchecked")
public class Graph<T extends Comparable<T>> {

    private List<Vertex<T>> allVertices = new ArrayList<Vertex<T>>();
    private List<Edge<T>> allEdges = new ArrayList<Edge<T>>();

    public enum TYPE {
        DIRECTED, UNDIRECTED
    }

    /** Defaulted to undirected */
    private TYPE type = TYPE.UNDIRECTED;

    public Graph() { }

    public Graph(TYPE type) {
        this.type = type;
    }

    /** Deep copies **/
    public Graph(Graph<T> g) {
        type = g.getType();

        // Copy the vertices which also copies the edges
        for (Vertex<T> v : g.getVertices())
            this.allVertices.add(new Vertex<T>(v));

        for (Vertex<T> v : this.getVertices()) {
            for (Edge<T> e : v.getEdges()) {
                this.allEdges.add(e);
            }
        }
    }

    /**
     * Creates a Graph from the vertices and edges. This defaults to an undirected Graph
     *
     * NOTE: Duplicate vertices and edges ARE allowed.
     * NOTE: Copies the vertex and edge objects but does NOT store the Collection parameters itself.
     *
     * @param vertices Collection of vertices
     * @param edges Collection of edges
     */
    public Graph(Collection<Vertex<T>> vertices, Collection<Edge<T>> edges) {
        this(TYPE.UNDIRECTED, vertices, edges);
    }

    /**
     * Creates a Graph from the vertices and edges.
     *
     * NOTE: Duplicate vertices and edges ARE allowed.
     * NOTE: Copies the vertex and edge objects but does NOT store the Collection parameters itself.
     *
     * @param vertices Collection of vertices
     * @param edges Collection of edges
     */
    public Graph(TYPE type, Collection<Vertex<T>> vertices, Collection<Edge<T>> edges) {
        this(type);

        this.allVertices.addAll(vertices);
        this.allEdges.addAll(edges);

        for (Edge<T> e : edges) {
            final Vertex<T> from = e.from;
            final Vertex<T> to = e.to;

            if (!this.allVertices.contains(from) || !this.allVertices.contains(to))
                continue;

            from.addEdge(e);
            if (this.type == TYPE.UNDIRECTED) {
                Edge<T> reciprical = new Edge<T>(e.cost, to, from);
                to.addEdge(reciprical);
                this.allEdges.add(reciprical);
            }
        }
    }

    public TYPE getType() {
        return type;
    }

    public void addEdge(Edge<T> edge) {
        this.allEdges.add(edge);
        final Vertex<T> from = edge.from;
        final Vertex<T> to = edge.to;

        if (!this.allVertices.contains(from) || !this.allVertices.contains(to)){
            System.out.println("Vertices not avaliable!");
            return;
        }

        from.addEdge(edge);
        if (this.type == TYPE.UNDIRECTED) {
            Edge<T> reciprical = new Edge<T>(edge.cost, to, from);
            to.addEdge(reciprical);
            this.allEdges.add(reciprical);
        }
    }

    public void addVertix(Vertex<T> vertix) {
        this.allVertices.add(vertix);
    }

    public List<Vertex<T>> getVertices() {
        return allVertices;
    }

    public List<Edge<T>> getEdges() {
        return allEdges;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int code = this.type.hashCode() + this.allVertices.size() + this.allEdges.size();
        for (Vertex<T> v : allVertices)
            code *= v.hashCode();
        for (Edge<T> e : allEdges)
            code *= e.hashCode();
        return 31 * code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object g1) {
        if (!(g1 instanceof Graph))
            return false;

        final Graph<T> g = (Graph<T>) g1;

        final boolean typeEquals = this.type == g.type;
        if (!typeEquals)
            return false;

        final boolean verticesSizeEquals = this.allVertices.size() == g.allVertices.size();
        if (!verticesSizeEquals)
            return false;

        final boolean edgesSizeEquals = this.allEdges.size() == g.allEdges.size();
        if (!edgesSizeEquals)
            return false;

        // Vertices can contain duplicates and appear in different order but both arrays should contain the same elements
        final Object[] ov1 = this.allVertices.toArray();
        Arrays.sort(ov1);
        final Object[] ov2 = g.allVertices.toArray();
        Arrays.sort(ov2);
        for (int i=0; i<ov1.length; i++) {
            final Vertex<T> v1 = (Vertex<T>) ov1[i];
            final Vertex<T> v2 = (Vertex<T>) ov2[i];
            if (!v1.equals(v2))
                return false;
        }

        // Edges can contain duplicates and appear in different order but both arrays should contain the same elements
        final Object[] oe1 = this.allEdges.toArray();
        Arrays.sort(oe1);
        final Object[] oe2 = g.allEdges.toArray();
        Arrays.sort(oe2);
        for (int i=0; i<oe1.length; i++) {
            final Edge<T> e1 = (Edge<T>) oe1[i];
            final Edge<T> e2 = (Edge<T>) oe2[i];
            if (!e1.equals(e2))
                return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (Vertex<T> v : allVertices)
            builder.append(v.toString());
        return builder.toString();
    }

    public boolean isFullEdges(){
        if(this.getEdges().size()==(this.getVertices().size()*(this.getVertices().size()-1)))
            return true;
        return false;
    }

    public boolean contain_edges(Edge<T> input_edge){
        for (Graph.Edge<T> edge : this.allEdges) {
            if(edge.getToVertex().value==input_edge.getToVertex().value && edge.getFromVertex().value==input_edge.getFromVertex().value)
                return true;
        }
        return false;
    }

    public int get_index_vertex(Vertex<T> input){
        int i=0;
        for (Vertex<T> vertex: getVertices()){
            if(vertex.value==input.value)
                return i;
            i++;
        }
        return -1;
    }

    public Graph<T> transpose(){
        List<Edge<T>>Edges_temp= new ArrayList<>();
        List<Vertex<T>>Vertex_temp=new ArrayList<>();
        for (Vertex<T> vertex:getVertices()){
            Vertex_temp.add(new Vertex(vertex.value));
        }
        for(Edge<T> edge : getEdges()){
            Edges_temp.add(new Edge<>(edge.cost,Vertex_temp.get(get_index_vertex(edge.to)),Vertex_temp.get(get_index_vertex(edge.from))));
        }
        return new Graph<T>(getType(),Vertex_temp,Edges_temp);
    }

    public boolean replace_contain_vertex(Graph.Vertex<T> input, List<Graph.Vertex<T>> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).value == input.value) {
                return true;
            }
        }
        return false;
    }

    public boolean replace_contain_edge(Graph.Edge<T> input, List<Graph.Edge<T>> list) {
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).to == input.to) && (list.get(i).from == input.from)) {
                return true;
            }
        }
        return false;
    }

    /*
        Function 1: this function checks if there is a cycle in the
                    graph and returns true or false
     */
    public boolean isCyclic() {
        if (allVertices == null)
            throw new IllegalArgumentException("Graph is NULL.");

        List<Vertex<T>> visitedVerticies = new ArrayList<Vertex<T>>();
        List<Edge<T>> visitedEdges = new ArrayList<Edge<T>>();

        if (allVertices == null || allVertices.size() == 0)
            return false;

        // Select the zero-ith element as the root
        Graph.Vertex<T> root = allVertices.get(0);
        boolean result = depthFirstSearch(root, visitedVerticies, visitedEdges);
        if(result) {
            //System.out.println("--");
            //System.out.println(visitedEdges);
           // System.out.println("--");
        }
        return result;
    }

    public boolean depthFirstSearch(Vertex<T> vertex, List<Vertex<T>> visitedVerticies, List<Edge<T>> visitedEdges) {
        if (!replace_contain_vertex(vertex,visitedVerticies)) {
            // Found an unvisited, add to the set
            visitedVerticies.add(vertex);

            if (!vertex.getEdges().isEmpty()) {
                // Follow each unvisited edge, visit the vertex the edge connects to.
                for (Graph.Edge<T> edge : vertex.getEdges()) {
                    boolean result;
                    if (!replace_contain_edge(edge,visitedEdges)) {
                        visitedEdges.add(edge);
                        Vertex<T> to=getVertices().get(get_index_vertex(edge.getToVertex()));
                        result = depthFirstSearch(to, new ArrayList<Vertex<T>>(visitedVerticies), visitedEdges);
                        if (result)
                            return true;
                    }
                }
            }
        } else {
            // visited
            return true;
        }
        return false;
    }

    /*
        Function 2: this function checks if the graph is strongly connected or not and
                    returns the boolean value depending upon the processing
     */




    public boolean isStronglyConnected(){
        if(checked())
            return transpose().checked();
        return false;
    }

    public boolean checked(){
        int size = this.allVertices.size(); //get total of number of vertex
        List<Graph.Vertex<T>> closedSet = new ArrayList<Graph.Vertex<T>>(size); // The set of vertex that already evaluated.
        List<Graph.Vertex<T>> openSet = new ArrayList<Graph.Vertex<T>>(size); // The set of vertex to be evaluated, initially containing the start vertex
        openSet.add(allVertices.get(0));
        while (!openSet.isEmpty()) {
            Graph.Vertex<T> current = openSet.get(0);
            openSet.remove(0);
            closedSet.add(current);
            for (Graph.Edge<T> edge : current.getEdges()) {
                Graph.Vertex<T> neighbor;
                neighbor = getVertices().get(get_index_vertex(edge.getToVertex()));
                if (replace_contain_vertex(neighbor,closedSet))
                    continue;
                if (!replace_contain_vertex(neighbor,openSet))
                    openSet.add(neighbor);
            }
        }
        //System.out.println("closedSet.size() = "+closedSet.size());
        if(closedSet.size()==size) {
            return true;
        }
        return false;
    }

    /*
        Funcion 3: this function traverse through the graph to find the shortest path between
                    two points and return the list of nodes leads from starting to ending point
     */
    public List<Graph.Edge<T>> aStar(Graph.Vertex<T> start, Graph.Vertex<T> goal) {
        final int size = allVertices.size(); // used to size data structures appropriately
        final Set<Graph.Vertex<T>> closedSet = new HashSet<Graph.Vertex<T>>(size); // The set of nodes already evaluated.
        final List<Graph.Vertex<T>> openSet = new ArrayList<Graph.Vertex<T>>(size); // The set of tentative nodes to be evaluated, initially containing the start node
        openSet.add(start);
        final Map<Graph.Vertex<T>,Graph.Vertex<T>> cameFrom = new HashMap<Graph.Vertex<T>,Graph.Vertex<T>>(size); // The map of navigated nodes.

        final Map<Graph.Vertex<T>,Integer> gScore = new HashMap<Graph.Vertex<T>,Integer>(); // Cost from start along best known path.
        gScore.put(start, 0);

        // Estimated total cost from start to goal through y.
        final Map<Graph.Vertex<T>,Integer> fScore = new HashMap<Graph.Vertex<T>,Integer>();
        for (Graph.Vertex<T> v : allVertices)
            fScore.put(v, Integer.MAX_VALUE);
        fScore.put(start, heuristicCostEstimate(start,goal));

        final Comparator<Graph.Vertex<T>> comparator = new Comparator<Graph.Vertex<T>>() {
            /**
             * {@inheritDoc}
             */
            @Override
            public int compare(Graph.Vertex<T> o1, Graph.Vertex<T> o2) {
                if (fScore.get(o1) < fScore.get(o2))
                    return -1;
                if (fScore.get(o2) < fScore.get(o1))
                    return 1;
                return 0;
            }
        };

        while (!openSet.isEmpty()) {
            final Graph.Vertex<T> current = openSet.get(0);
            if (current.equals(goal))
                return reconstructPath(cameFrom, goal);

            openSet.remove(0);
            closedSet.add(current);
            for (Graph.Edge<T> edge : current.getEdges()) {
                final Graph.Vertex<T> neighbor = getVertices().get(get_index_vertex(edge.getToVertex()));
                if (closedSet.contains(neighbor))
                    continue; // Ignore the neighbor which is already evaluated.

                final int tenativeGScore = gScore.get(current) + distanceBetween(current,neighbor); // length of this path.
                if (!openSet.contains(neighbor))
                    openSet.add(neighbor); // Discover a new node
                else if (tenativeGScore >= gScore.get(neighbor))
                    continue;

                // This path is the best until now. Record it!
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tenativeGScore);
                final int estimatedFScore = gScore.get(neighbor) + heuristicCostEstimate(neighbor, goal);
                fScore.put(neighbor, estimatedFScore);

                // fScore has changed, re-sort the list
                Collections.sort(openSet,comparator);
            }
        }

        return null;
    }

    /**
     * Default distance is the edge cost. If there is no edge between the start and next then
     * it returns Integer.MAX_VALUE;
     */
    protected int distanceBetween(Graph.Vertex<T> start, Graph.Vertex<T> next) {
        for (Graph.Edge<T> e : start.getEdges()) {
            if (e.getToVertex().equals(next))
                return e.getCost();
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Default heuristic: cost to each vertex is 1.
     */
    @SuppressWarnings("unused")
    protected int heuristicCostEstimate(Graph.Vertex<T> start, Graph.Vertex<T> goal) {
        return 1;
    }

    private List<Graph.Edge<T>> reconstructPath(Map<Graph.Vertex<T>, Graph.Vertex<T>> cameFrom, Graph.Vertex<T> current) {
        final List<Graph.Edge<T>> totalPath = new ArrayList<Graph.Edge<T>>();

        while (current != null) {
            final Graph.Vertex<T> previous = current;
            current = cameFrom.get(current);
            if (current != null) {
                final Graph.Edge<T> edge = current.getEdge(previous);
                totalPath.add(edge);
            }
        }
        Collections.reverse(totalPath);
        return totalPath;
    }

    public static class Vertex<T extends Comparable<T>> implements Comparable<Vertex<T>> {

        private T value = null;
        private int weight = 0;
        private List<Edge<T>> edges = new ArrayList<Edge<T>>();

        public Vertex(T value) {
            this.value = value;
        }

        public Vertex(T value, int weight) {
            this(value);
            this.weight = weight;
        }

        /** Deep copies the edges along with the value and weight **/
        public Vertex(Vertex<T> vertex) {
            this(vertex.value, vertex.weight);

            this.edges.addAll(vertex.edges);
        }

        public T getValue() {
            return value;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public void addEdge(Edge<T> e) {
            edges.add(e);
        }

        public List<Edge<T>> getEdges() {
            return edges;
        }

        public Edge<T> getEdge(Vertex<T> v) {
            for (Edge<T> e : edges) {
                if (e.to.getValue().equals(v.getValue()))
                    return e;
            }
            return null;
        }

        public boolean pathTo(Vertex<T> v) {
            for (Edge<T> e : edges) {
                if (e.to.equals(v))
                    return true;
            }
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            final int code = this.value.hashCode() + this.weight + this.edges.size();
            return 31 * code;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object v1) {
            if (!(v1 instanceof Vertex))
                return false;

            final Vertex<T> v = (Vertex<T>) v1;

            final boolean weightEquals = this.weight == v.weight;
            if (!weightEquals)
                return false;

            final boolean edgesSizeEquals = this.edges.size() == v.edges.size();
            if (!edgesSizeEquals)
                return false;

            final boolean valuesEquals = this.value.equals(v.value);
            if (!valuesEquals)
                return false;

            final Iterator<Edge<T>> iter1 = this.edges.iterator();
            final Iterator<Edge<T>> iter2 = v.edges.iterator();
            while (iter1.hasNext() && iter2.hasNext()) {
                // Only checking the cost
                final Edge<T> e1 = iter1.next();
                final Edge<T> e2 = iter2.next();
                if (e1.cost != e2.cost)
                    return false;
            }

            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(Vertex<T> v) {
            final int valueComp = this.value.compareTo(v.value);
            if (valueComp != 0)
                return valueComp;

            if (this.weight < v.weight)
                return -1;
            if (this.weight > v.weight)
                return 1;

            if (this.edges.size() < v.edges.size())
                return -1;
            if (this.edges.size() > v.edges.size())
                return 1;

            final Iterator<Edge<T>> iter1 = this.edges.iterator();
            final Iterator<Edge<T>> iter2 = v.edges.iterator();
            while (iter1.hasNext() && iter2.hasNext()) {
                // Only checking the cost
                final Edge<T> e1 = iter1.next();
                final Edge<T> e2 = iter2.next();
                if (e1.cost < e2.cost)
                    return -1;
                if (e1.cost > e2.cost)
                    return 1;
            }

            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Value=").append(value).append(" weight=").append(weight).append("\n");
            for (Edge<T> e : edges)
                builder.append("\t").append(e.toString());
            return builder.toString();
        }
    }

    public static class Edge<T extends Comparable<T>> implements Comparable<Edge<T>> {

        private Vertex<T> from = null;
        private Vertex<T> to = null;
        private int cost = 0;

        public Edge(int cost, Vertex<T> from, Vertex<T> to) {
            if (from == null || to == null)
                throw (new NullPointerException("Both 'to' and 'from' vertices need to be non-NULL."));

            if (from == to) {
                try {
                    throw (new NullPointerException());
                } catch(NullPointerException e) {
                    System.out.println("Self Edges are not allowed");
                }
            } else {
                this.cost = cost;
                this.from = from;
                this.to = to;
            }
        }

        public Edge(Edge<T> e) {
            this(e.cost, e.from, e.to);
        }

        public int getCost() {
            return cost;
        }

        public void setCost(int cost) {
            this.cost = cost;
        }

        public Vertex<T> getFromVertex() {
            return from;
        }

        public Vertex<T> getToVertex() {
            return to;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            final int cost = (this.cost * (this.getFromVertex().hashCode() * this.getToVertex().hashCode()));
            return 31 * cost;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object e1) {
            if (!(e1 instanceof Edge))
                return false;

            final Edge<T> e = (Edge<T>) e1;

            final boolean costs = this.cost == e.cost;
            if (!costs)
                return false;

            final boolean from = this.from.equals(e.from);
            if (!from)
                return false;

            final boolean to = this.to.equals(e.to);
            if (!to)
                return false;

            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo(Edge<T> e) {
            if (this.cost < e.cost)
                return -1;
            if (this.cost > e.cost)
                return 1;

            final int from = this.from.compareTo(e.from);
            if (from != 0)
                return from;

            final int to = this.to.compareTo(e.to);
            if (to != 0)
                return to;

            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            try {
                builder.append("[ ").append(from.value).append("(").append(from.weight).append(") ").append("]").append(" -> ")
                        .append("[ ").append(to.value).append("(").append(to.weight).append(") ").append("]").append(" = ").append(cost).append("\n");
            } catch (NullPointerException e) {

            }

            return builder.toString();
        }
    }
}