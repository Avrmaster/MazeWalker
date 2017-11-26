package ua.leskivproduction.kma.mazewalker.model;

public class UndirectedGraph extends Graph{

    public UndirectedGraph(int V) {
        super(V);
    }

    public void addEdge(int v1, int v2) {
        super.addEdge(v1, v2);
        super.addEdge(v2, v1);
    }
}
