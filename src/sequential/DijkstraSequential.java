package sequential;

import model.Graph;
import model.Node;

import java.util.*;

public class DijkstraSequential {

    private Set<Integer> visited = new HashSet<>();
    private PriorityQueue<Node> queue = new PriorityQueue<>();
    private Graph graph;
    private List<Integer> distances;

    private void initData(){
        distances = new ArrayList<>(graph.getNumberOfNodes());
        visited.clear();
        queue.clear();
        for (int i = 0; i < graph.getNumberOfNodes(); i++) {
            distances.add(i, Integer.MAX_VALUE);
        }
        queue.add(new Node(graph.getSourceNode(), 0));
        distances.set(graph.getSourceNode(), 0);
    }

    public List<Integer> solve(Graph graph) {
        this.graph = graph;
        initData();
        while (!queue.isEmpty()) {
            int node = queue.remove().getNode();
            if (!visited.contains(node)) {
                visited.add(node);
                processNeighbours(node, distances);
            }
        }
        return distances;
    }

    private void processNeighbours(int node, List<Integer> distances) {
        List<Integer> neighbours = graph.getEdges().get(node);
        for (int i = 0; i < neighbours.size(); i++) {
            if (!visited.contains(i) && neighbours.get(i) > 0) {
                int newDistance = distances.get(node) + neighbours.get(i);
                if (newDistance < distances.get(i)) {
                    distances.set(i, newDistance);
                    queue.add(new Node(i, newDistance));
                }
            }
        }
    }
}
