// BFS.java
package org.example.traversal;

import org.example.common.*;

import java.util.*;

public class BFS {

    public Graph graph;
    public List<Node> bfsOrder;
    public Set<Edge> bfsPathEdges;

    public BFS(Graph graph) {
        this.graph = graph;
        this.bfsOrder = new ArrayList<>();
        this.bfsPathEdges = new HashSet<>();
    }

    // Perform BFS starting from a specific node
    public void performBFS(String startNodeId) {
        Node startNode = findNodeById(startNodeId);

        if (startNode == null) {
            return; // Starting node not found
        }

        Set<String> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        queue.add(startNode);
        visited.add(startNode.id);
        parentMap.put(startNode.id, null);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            bfsOrder.add(current); // Add the node to the BFS order list

            // Visit all neighbors of the current node
            for (Edge edge : graph.edges) {
                Node neighbor = null;
                if (edge.source.equals(current.id) && !visited.contains(edge.target)) {
                    neighbor = findNodeById(edge.target);
                } else if (edge.target.equals(current.id) && !visited.contains(edge.source)) {
                    neighbor = findNodeById(edge.source);
                }

                if (neighbor != null) {
                    queue.add(neighbor);
                    visited.add(neighbor.id);
                    parentMap.put(neighbor.id, current.id);
                    bfsPathEdges.add(edge); // Add the edge to the BFS path edges
                }
            }
        }

        // Print the BFS path edges
        System.out.println("BFS Path Edges:");
        for (Edge edge : bfsPathEdges) {
            System.out.println("Edge from " + edge.source + " to " + edge.target);
        }
    }

    private Node findNodeById(String id) {
        return graph.nodes.stream()
                .filter(node -> node.id.equals(id))
                .findFirst()
                .orElse(null);
    }
}
