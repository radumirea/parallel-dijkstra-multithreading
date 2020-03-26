package parallel;

import model.Graph;
import model.Node;

import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class DijkstraParallel {

    private Graph graph;
    private Set<Integer> visited = new HashSet<>();
    private List<PriorityQueue<Node>> queues = new ArrayList<>();
    private List<Integer> distances;
    private AtomicBoolean isFinished;
    private Node currentNode;

    private void initData() {
        distances = new ArrayList<>(graph.getNumberOfNodes());
        visited.clear();
        queues.clear();
        for (int i = 0; i < graph.getNumberOfNodes(); i++) {
            distances.add(i, Integer.MAX_VALUE);
        }
        distances.set(graph.getSourceNode(), 0);
        isFinished = new AtomicBoolean(false);
        currentNode = new Node(graph.getSourceNode(), 0);
    }

    public List<Integer> solve(Graph graph, int nrThreads) {
        this.graph = graph;
        initData();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < nrThreads; i++) {
            PriorityQueue<Node> localQueue = new PriorityQueue<>();
            queues.add(localQueue);
        }
        ReduceRunnable reduceRunnable = new ReduceRunnable(queues, isFinished, visited, currentNode);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(nrThreads, reduceRunnable);
        int start;
        int end = 0;
        int chunk = graph.getNumberOfNodes() / nrThreads;
        int leftover = graph.getNumberOfNodes() % nrThreads;
        //each thread gets an equal chunk of data
        for (int i = 0; i < nrThreads; i++) {
            start = end;
            end += chunk;
            if (leftover > 0) {
                end++;
                leftover--;
            }
            Thread dijThread = new DijkstraThread(graph, start, end, visited, cyclicBarrier, queues.get(i), distances, isFinished, currentNode);
            dijThread.start();
            threads.add(dijThread);
        }
        //waiting for all threads to finish
        for (int i = 0; i < nrThreads; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return distances;
    }

    //reduce operation which will be called after each iteration of the algorithm
    public static class ReduceRunnable implements Runnable {

        private List<PriorityQueue<Node>> queues;
        private AtomicBoolean isFinished;
        private Set<Integer> visited;
        private Node currentNode;

        public ReduceRunnable(List<PriorityQueue<Node>> queues, AtomicBoolean isFinished, Set<Integer> visited, Node currentNode) {
            this.queues = queues;
            this.isFinished = isFinished;
            this.visited = visited;
            this.currentNode = currentNode;
        }

        @Override
        //computes and broadcasts the global minimum node
        public void run() {
            while (true) {
                Node minNode = null;
                int queueIndex = 0;
                //search all queues for the global minimum node
                for (int i = 0; i < queues.size(); i++) {
                    if (!queues.get(i).isEmpty()) {
                        Node node = queues.get(i).peek();
                        if (minNode == null || node.compareTo(minNode) < 0) {
                            minNode = node;
                            queueIndex = i;
                        }
                    }
                }
                if (minNode == null) { //all queues are empty, algorithm is finished
                    isFinished.set(true);
                    return;
                } else if (!visited.contains(minNode.getNode())) { //found global minimum
                    visited.add(minNode.getNode());
                    currentNode.setNode(minNode.getNode());
                    currentNode.setDistance(minNode.getDistance());
                    queues.get(queueIndex).remove();
                    return;
                } else { //current global minimum node is visited, remove it and try again
                    queues.get(queueIndex).remove();
                }
            }
        }
    }
}
