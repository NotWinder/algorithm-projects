// Kosaraju.java
package org.example.traversal;

import org.example.common.*;

import java.util.*;

public class Kosaraju {

    private Graph graph;
    private List<List<Node>> stronglyConnectedComponents; // Stores all SCCs

    public Kosaraju(Graph graph) {
        this.graph = graph;
        this.stronglyConnectedComponents = new ArrayList<>();
    }

    public List<List<Node>> getStronglyConnectedComponents() {
        return stronglyConnectedComponents;
    }

    // Step 1: Perform DFS and store the nodes in a stack based on their finish time
    private Stack<Node> fillOrder() {
        Stack<Node> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        for (Node node : graph.nodes) {
            if (!visited.contains(node.id)) {
                fillOrderDFS(node, visited, stack);
            }
        }

        return stack;
    }

    private void fillOrderDFS(Node node, Set<String> visited, Stack<Node> stack) {
        visited.add(node.id);

        for (Edge edge : graph.edges) {
            if (edge.source.equals(node.id)) {
                Node neighbor = findNodeById(edge.target);
                if (neighbor != null && !visited.contains(neighbor.id)) {
                    fillOrderDFS(neighbor, visited, stack);
                }
            }
        }

        stack.push(node); // Push node to stack after visiting all its neighbors
    }

    // Step 2: Transpose the graph (reverse all edges)
    public Graph transposeGraph(Graph graph) {
        // Kosaraju.java (where you create Graph instance)
        List<Node> nodes = new ArrayList<>(); // You can populate this list with your nodes
        List<Edge> edges = new ArrayList<>(); // Populate this with edges
        Graph transposedGraph = new Graph(nodes, edges);

        transposedGraph.nodes = new ArrayList<>(graph.nodes); // Copy nodes
        transposedGraph.edges = new ArrayList<>(); // Initialize edges

        for (Edge edge : graph.edges) {
            Edge reversedEdge = new Edge(edge.target, edge.source); // Reverse the direction
            transposedGraph.edges.add(reversedEdge);
        }

        return transposedGraph;
    }

    // Step 3: Perform DFS on the transposed graph in the order of the stack
    private void dfsOnTransposed(Node node, Set<String> visited, List<Node> scc, Graph transposedGraph) {
        visited.add(node.id);
        scc.add(node);

        for (Edge edge : transposedGraph.edges) {
            if (edge.source.equals(node.id)) {
                Node neighbor = findNodeById(transposedGraph, edge.target);
                if (neighbor != null && !visited.contains(neighbor.id)) {
                    dfsOnTransposed(neighbor, visited, scc, transposedGraph);
                }
            }
        }
    }

    // Perform Kosaraju's Algorithm
    public void performKosaraju() {
        stronglyConnectedComponents.clear();
        Stack<Node> stack = fillOrder(); // Step 1

        Graph transposedGraph = transposeGraph(this.graph); // Pass the current graph

        Set<String> visited = new HashSet<>();
        while (!stack.isEmpty()) {
            Node node = stack.pop();

            if (!visited.contains(node.id)) {
                List<Node> scc = new ArrayList<>();
                dfsOnTransposed(node, visited, scc, transposedGraph); // Step 3
                stronglyConnectedComponents.add(scc);
            }
        }
    }

    // Helper method to find a node by ID in the transposed graph
    private Node findNodeById(Graph graph, String id) {
        return graph.nodes.stream()
                .filter(node -> node.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    private Node findNodeById(String id) {
        return findNodeById(graph, id);
    }
}
