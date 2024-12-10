// DFS.java
package org.example.traversal;

import org.example.common.*;

import java.util.*;

public class DFS {

    public Graph graph;
    public List<Node> dfsOrder;
    public Set<Edge> dfsPathEdges;

    public DFS(Graph graph) {
        this.graph = graph;
        this.dfsOrder = new ArrayList<>();
        this.dfsPathEdges = new HashSet<>();
    }

    // Perform DFS starting from a specific node
    public void performDFS(String startNodeId) {
        Node startNode = findNodeById(startNodeId);

        if (startNode == null) {
            return; // Starting node not found
        }

        Set<String> visited = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        Map<String, String> parentMap = new HashMap<>();
        stack.push(startNode);
        visited.add(startNode.id);
        parentMap.put(startNode.id, null);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            dfsOrder.add(current); // Add the node to the DFS order list

            // Visit all neighbors of the current node
            for (Edge edge : graph.edges) {
                Node neighbor = null;
                if (edge.source.equals(current.id) && !visited.contains(edge.target)) {
                    neighbor = findNodeById(edge.target);
                    if (neighbor != null) {
                        stack.push(neighbor);
                        visited.add(neighbor.id);
                        parentMap.put(neighbor.id, current.id);
                        dfsPathEdges.add(edge); // Add the edge to the DFS path edges
                    }
                } else if (edge.target.equals(current.id) && !visited.contains(edge.source)) {
                    neighbor = findNodeById(edge.source);
                    if (neighbor != null) {
                        stack.push(neighbor);
                        visited.add(neighbor.id);
                        parentMap.put(neighbor.id, current.id);
                        dfsPathEdges.add(edge); // Add the edge to the DFS path edges
                    }
                }
            }
        }
    }

    private Node findNodeById(String id) {
        return graph.nodes.stream()
                .filter(node -> node.id.equals(id))
                .findFirst()
                .orElse(null);
    }
}
