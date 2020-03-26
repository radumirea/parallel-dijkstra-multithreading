package parallel;

import model.Graph;
import model.Node;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class DijkstraThread extends Thread {

    private Graph graph;
    private int start;
    private int end;
    private Set<Integer> visited;
    private CyclicBarrier cyclicBarrier;
    private PriorityQueue<Node> localQueue;
    private final List<Integer> distances;
    private AtomicBoolean isFinished;
    private Node currentNode;

    public DijkstraThread(Graph graph, int start, int end, Set<Integer> visited, CyclicBarrier cyclicBarrier, PriorityQueue<Node> localQueue,
                          List<Integer> distances, AtomicBoolean isFinished, Node currentNode) {
        this.graph = graph;
        this.start = start;
        this.end = end;
        this.visited = visited;
        this.cyclicBarrier = cyclicBarrier;
        this.localQueue = localQueue;
        this.distances = distances;
        this.isFinished = isFinished;
        this.currentNode = currentNode;
    }

    @Override
    public void run() {
        while (!isFinished.get()) {
            processNeighbours(currentNode.getNode());
            try {
                //wait for all threads to finish the iteration
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private void processNeighbours(int node) {
        List<Integer> neighbours = graph.getEdges().get(node);
        //process only the local nodes
        for (int i = start; i < end; i++) {
            if (!visited.contains(i) && neighbours.get(i) > 0) {
                int newDistance = distances.get(node) + neighbours.get(i);
                if (newDistance < distances.get(i)) {
                    distances.set(i, newDistance);
                    localQueue.add(new Node(i, newDistance));
                }
            }
        }
    }
}
