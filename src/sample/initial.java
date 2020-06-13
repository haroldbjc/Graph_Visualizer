package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class initial {
    Random rand = new Random();
    List<Graph.Vertex<String>> verticies = new ArrayList<Graph.Vertex<String>>();
    Graph.Vertex<String> v1 = new Graph.Vertex<String>("AU");
    Graph.Vertex<String> v2 = new Graph.Vertex<String>("EG");
    Graph.Vertex<String> v3 = new Graph.Vertex<String>("BE");
    Graph.Vertex<String> v4 = new Graph.Vertex<String>("DK");
    Graph.Vertex<String> v5 = new Graph.Vertex<String>("HK");
    {
        verticies.add(v1);
        verticies.add(v2);
        verticies.add(v3);
        verticies.add(v4);
        verticies.add(v5);
    }

    List<Graph.Edge<String>> edges = new ArrayList<Graph.Edge<String>>();
    Graph.Edge<String> e1_5 = new Graph.Edge<String>(6, v1, v5);
    Graph.Edge<String> e1_2 = new Graph.Edge<String>(12, v1, v2);
    Graph.Edge<String> e5_3 = new Graph.Edge<String>(9, v5, v3);
    Graph.Edge<String> e4_3 = new Graph.Edge<String>(1, v4, v3);
    Graph.Edge<String> e4_2 = new Graph.Edge<String>(4, v4, v2);
    {
        edges.add(e1_5);
        edges.add(e1_2);
        edges.add(e5_3);
        edges.add(e4_3);
        edges.add(e4_2);
    }
    Graph<String> initialGraph = new Graph<String>(Graph.TYPE.DIRECTED, verticies, edges); //making new graph
}