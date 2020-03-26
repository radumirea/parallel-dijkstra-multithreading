import model.Graph;
import parallel.DijkstraParallel;
import sequential.DijkstraSequential;
import util.GraphUtils;
import util.IOUtils;
import validation.InvalidDataException;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Main {

    private static final String INPUT_FILE = "input.txt";

    public static void main(String[] args) {
        Graph graph;
        List<Integer> results = new ArrayList<>();
        try {
            //if run with arguments
            if (args.length > 0) {
                if (args[0].equals("-seq")) { //run the sequential method
                    if (args.length > 2) { //with graph generation
                        graph = GraphUtils.generateGraphMatrix(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                        IOUtils.writeGraph(INPUT_FILE, graph);
                    } else {
                        graph = IOUtils.readGraph(INPUT_FILE);
                    }
                    //run and print execution time
                    results = runSequentialMethod(graph);
                } else if (args[0].equals("-par") && args.length > 1) { //run the parallel method
                    int nrThreads;
                    if (args.length > 3) { //with graph generation
                        graph = GraphUtils.generateGraphMatrix(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        IOUtils.writeGraph(INPUT_FILE, graph);
                    } else {
                        graph = IOUtils.readGraph(INPUT_FILE);
                    }
                    nrThreads = Integer.parseInt(args[1]);
                    //run and print execution time
                    results = runParallelMethod(graph, nrThreads);
                } else {
                    System.out.println("Please specify the type of execution (sequential or parallel)");
                }
            } else {
                //no arguments, default to the parallel method with 4 threads
                graph = IOUtils.readGraph(INPUT_FILE);
                runParallelMethod(graph, 4);
            }
            IOUtils.writeResults("output.txt", results);
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Invalid command format");
        }
    }

    private static List<Integer> runSequentialMethod(Graph graph){
        DijkstraSequential dijkstraSequential = new DijkstraSequential();
        Instant start = Instant.now();
        List<Integer> results = dijkstraSequential.solve(graph);
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println(timeElapsed);
        return results;
    }

    private static List<Integer> runParallelMethod(Graph graph, int nrThreads){
        DijkstraParallel dijkstraParallel = new DijkstraParallel();
        Instant start = Instant.now();
        List<Integer> results = dijkstraParallel.solve(graph, nrThreads);
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println(timeElapsed);
        return results;
    }
}
