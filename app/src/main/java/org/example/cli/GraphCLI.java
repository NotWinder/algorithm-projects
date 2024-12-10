package org.example.cli;

import org.example.common.*;
import org.example.traversal.*;
import org.example.visualization.*;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GraphCLI {

    private static Graph graph;
    private static GraphRenderer renderer;

    public static void main(String[] args) {
        try {
            // Read and parse the graph JSON file
            String content = new String(Files.readAllBytes(Paths.get("graph.json")));
            Gson gson = new Gson();
            graph = gson.fromJson(content, Graph.class);

            // Start the command-line interface
            runCLI();
        } catch (IOException e) {
            System.err.println("Error reading graph file: " + e.getMessage());
        }
    }

    private static void runCLI() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Initialize the renderer with the graph
        renderer = new GraphRenderer(graph);

        while (running) {
            System.out.println("\nChoose an action:");
            System.out.println("1. BFS");
            System.out.println("2. DFS");
            System.out.println("3. Planarity Test");
            System.out.println("4. Kosaraju's Algorithm");
            System.out.println("5. Topological Sort");
            System.out.println("6. Exit");

            // Read user's choice
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    performBFS(scanner);
                    break;
                case 2:
                    performDFS(scanner);
                    break;
                case 3:
                    performPlanarityTest();
                    break;
                case 4:
                    performKosaraju();
                    break;
                case 5:
                    performTopologicalSort();
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select again.");
            }
        }
        scanner.close();
    }

    private static void performBFS(Scanner scanner) {
        System.out.println("Enter starting node for BFS:");
        String startNode = scanner.nextLine();
        BFS bfs = new BFS(graph);
        bfs.performBFS(startNode);
        // Render BFS path
        try {
            renderer.renderGraph("bfs_graph.png", true, false, bfs.bfsPathEdges, null, bfs.bfsOrder, null);
            System.out.println("BFS completed and graph saved as 'bfs_graph.png'.");
        } catch (IOException e) {
            System.err.println("Error rendering BFS graph: " + e.getMessage());
        }
    }

    private static void performDFS(Scanner scanner) {
        System.out.println("Enter starting node for DFS:");
        String startNode = scanner.nextLine();
        DFS dfs = new DFS(graph);
        dfs.performDFS(startNode);
        // Render DFS path
        try {
            renderer.renderGraph("dfs_graph.png", false, true, null, dfs.dfsPathEdges, null, dfs.dfsOrder);
            System.out.println("DFS completed and graph saved as 'dfs_graph.png'.");
        } catch (IOException e) {
            System.err.println("Error rendering DFS graph: " + e.getMessage());
        }
    }

    private static void performPlanarityTest() {
        if (graph.isPlanar()) {
            System.out.println("The graph is planar.");
        } else {
            System.out.println("The graph is not planar.");
        }
    }

    private static void performKosaraju() {
        Kosaraju kosaraju = new Kosaraju(graph);
        kosaraju.performKosaraju();
        List<List<Node>> sccs = kosaraju.getStronglyConnectedComponents();
        System.out.println("Strongly Connected Components:");
        for (List<Node> scc : sccs) {
            System.out.println(scc);
        }
        // Render Kosaraju result
        try {
            renderer.renderKosaraju("kosaraju.png");
            System.out.println("Kosaraju's algorithm result saved as 'kosaraju.png'.");
        } catch (IOException e) {
            System.err.println("Error rendering Kosaraju's result: " + e.getMessage());
        }
    }

    private static void performTopologicalSort() {
        TopologicalSort topologicalSort = new TopologicalSort();
        if (topologicalSort.performTopologicalSort(graph)) {
            List<Node> topoOrder = topologicalSort.getTopoOrder();
            System.out.println("Topological Order: " + topoOrder);
            // Render Topological Sort
            try {
                renderer.setTopoOrder(topoOrder);
                renderer.renderTopologicalSort(graph, "topological_order.png");
                System.out.println("Topological Sort result saved as 'topological_order.png'.");
            } catch (IOException e) {
                System.err.println("Error rendering Topological Sort: " + e.getMessage());
            }
        } else {
            System.out.println("Topological sort failed or returned an empty order.");
        }
    }
}
