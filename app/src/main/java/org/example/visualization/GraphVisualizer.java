// GraphVisualizer.java
package org.example.visualization;

import com.google.gson.Gson;
import org.example.common.Graph;
import org.example.traversal.*;
import org.example.common.*;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GraphVisualizer {

    private Graph graph;
    // Fields in GraphVisualizer
    private TopologicalSort topologicalSort;
    private List<Node> topoOrder; // Topological order result

    public GraphVisualizer() {
        try {
            // Read and parse the JSON file
            String content = new String(Files.readAllBytes(Paths.get("graph.json")));
            Gson gson = new Gson();
            graph = gson.fromJson(content, Graph.class);

            // Initialize BFS and DFS
            BFS bfs = new BFS(graph);
            DFS dfs = new DFS(graph);
            // Perform Kosaraju's Algorithm for SCCs
            Kosaraju kosaraju = new Kosaraju(graph);
            kosaraju.performKosaraju();
            List<List<Node>> sccs = kosaraju.getStronglyConnectedComponents();

            // Log SCCs to the console
            System.out.println("Strongly Connected Components:");
            for (List<Node> scc : sccs) {
                System.out.println(scc);
            }

            // Perform BFS and DFS
            bfs.performBFS("0");
            dfs.performDFS("0");

            // Render the graphs
            GraphRenderer renderer = new GraphRenderer(graph);
            topologicalSort = new TopologicalSort();
            renderer.renderGraph("graph.png", false, false, bfs.bfsPathEdges, dfs.dfsPathEdges, bfs.bfsOrder,
                    dfs.dfsOrder);
            renderer.renderGraph("bfs_graph.png", true, false, bfs.bfsPathEdges, dfs.dfsPathEdges, bfs.bfsOrder,
                    dfs.dfsOrder);
            renderer.renderGraph("dfs_graph.png", false, true, bfs.bfsPathEdges, dfs.dfsPathEdges, bfs.bfsOrder,
                    dfs.dfsOrder);

            // Perform topological sort only once
            if (topologicalSort.performTopologicalSort(graph)) {
                topoOrder = topologicalSort.getTopoOrder();
                System.out.println("Topological order after sort: " + topoOrder);
                if (topoOrder != null && !topoOrder.isEmpty()) {
                    renderer.setTopoOrder(topoOrder);
                    renderer.renderTopologicalSort(graph, "topological_order.png");
                } else {
                    System.out.println("Topological sort failed or returned an empty order.");
                    JOptionPane.showMessageDialog(null, "Topological sort failed.");
                }
            }

            renderer.renderKosaraju("kosaraju.png");

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading graph file: " + e.getMessage());
        }
    }
}
