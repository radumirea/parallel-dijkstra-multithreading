package util;

import model.Graph;
import validation.InvalidDataException;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IOUtils {

    private static final String IO_ERROR = "IO Error: ";

    private IOUtils() {
    }

    public static void writeResults(String fileName, List<Integer> results) throws InvalidDataException {
        File file = new File(fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(file.toPath())) {
            for (Integer result : results) {
                String line;
                if (result < Integer.MAX_VALUE) {
                    line = String.valueOf(result);
                } else {
                    line = "Inf";
                }
                bufferedWriter.append(line).append('\n');
            }
        } catch (IOException e) {
            throw new InvalidDataException(IO_ERROR + e.getMessage());
        }
    }

    public static void writeGraph(String fileName, Graph graph) throws InvalidDataException {
        File file = new File(fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(file.toPath())) {
            bufferedWriter.append(String.valueOf(graph.getNumberOfNodes())).append(" ")
                    .append(String.valueOf(graph.getSourceNode())).append('\n');
            graph.getEdges().forEach(row -> {
                try {
                    for (Integer val : row) {
                        bufferedWriter.append(String.valueOf(val)).append(' ');
                    }
                    bufferedWriter.append('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            throw new InvalidDataException(IO_ERROR + e.getMessage());
        }
    }

    public static Graph readGraph(String fileName) throws InvalidDataException {
        List<List<Integer>> edges = new ArrayList<>();
        int source;
        int numberOfNodes;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line = bufferedReader.readLine();
            if (line != null) {
                String[] params = line.split(" ");
                numberOfNodes = Integer.parseInt(params[0]);
                source = Integer.parseInt(params[1]);
            } else {
                throw new InvalidDataException("Input file must specify the number of nodes and the source node");
            }
            while ((line = bufferedReader.readLine()) != null) {
                List<Integer> row = new ArrayList<>(numberOfNodes);
                String[] params = line.split(" ");
                for (String valS : params) {
                    int val = Integer.parseInt(valS);
                    row.add(val);
                }
                edges.add(row);
            }
        } catch (IOException e) {
            throw new InvalidDataException(IO_ERROR + e.getMessage());
        } catch (NumberFormatException e) {
            throw new InvalidDataException("Nodes and weights must be integers");
        }
        return new Graph(source, edges);
    }
}
