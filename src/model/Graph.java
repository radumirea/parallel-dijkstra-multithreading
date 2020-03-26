package model;

import java.util.List;

public class Graph {
    private int sourceNode;
    private List<List<Integer>> edges;
    private int numberOfNodes;

    public Graph(int sourceNode, List<List<Integer>> edges) {
        this.sourceNode = sourceNode;
        this.edges = edges;
        this.numberOfNodes = edges.size();
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public List<List<Integer>> getEdges() {
        return edges;
    }
}
