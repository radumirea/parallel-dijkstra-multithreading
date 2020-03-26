package model;

public class Node implements Comparable<Node> {
    private int node;
    private int distance;

    public Node(int node, int distance) {
        this.node = node;
        this.distance = distance;
    }

    public int getNode() {
        return node;
    }

    public int getDistance() {
        return distance;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Node node) {
        return Double.compare(this.distance, node.distance);
    }

    @Override
    public String toString() {
        return "Node{" +
                "node=" + node +
                ", distance=" + distance +
                '}';
    }
}
