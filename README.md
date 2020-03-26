# Parallel-Dijkstra-Java
Parallel implementation for Dijkstra algorithm using Java threads  
Input data will be taken from "input.txt" and ouput will be put in "output.txt"; it also prints out the execution time in milliseconds.  
You can run the jar in the following format:  
**java -jar ParallelDijkstra.jar [ -seq | (-par \<numberOfThreads\>) [ \<numberOfNodes\> \<edgeDensity\> ]]**  
Use *-seq* for sequential or *-par* for parallel. Input *numberOfNodes* and *edgeDensity* if you want to generate a new graph (in which case will be written in "input.txt")  
If no arguments are given, the default is a parallel run with 4 threads.  
Note: *edgeDensity* is represented in percentages and needs to be between 0 and 100.  
### Example  
**java -jar ParallelDijkstra.jar -par 4** -> will read from "input.txt" for input, execute parallel Dijkstra with 4 threads and write in "output.txt"  
**java -jar ParallelDijkstra.jar -seq 100 40** -> will generate a graph with 100 nodes and 40% edge density, write it in "input.txt", execute sequential Dijkstra and write the output in "output.txt"  
  
Warning: generating graphs with a large number of nodes may generate huge files (a file containing a graph of 5000 nodes is almost 90MB)
