package ua.leskivproduction.kma.mazewalker.model;

import java.util.HashSet;
import java.util.Set;

public class Graph {
    final public int V;
    protected int[] vertices;
    protected Set<Integer>[] connections;

    public Graph(int V) {
        this.V = V;
        this.vertices = new int[V];
        this.connections = (Set<Integer>[])new Set[V];
        for (int i = 0; i < V; i++)
            connections[i] = new HashSet<Integer>(V);
    }

    public void addEdge(int v1, int v2) {
        if (v1 < 0 || v1 >= V || v2 < 0 || v2 >= V)
            throw new IllegalArgumentException("Both vertices must exist in graph!");
        connections[v1].add(v2);
    }

    public int degree(int v){
        int degree = 0;
        for (int w : edges(v))
            degree++;
        return degree;
    }

    public Iterable<Integer> edges(int v){
        return connections[v];
    }

    public boolean hasEdge(int v1, int v2) {
        return connections[v1].contains(v2);
    }

}
