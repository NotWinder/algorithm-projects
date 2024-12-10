// TopologicalSort.java
package org.example.traversal;

import org.example.common.*;

import java.util.*;
import java.util.List;

public class TopologicalSort {

    private List<Node> topoOrder; // Stores the topological order
    private boolean hasCycle; // Indicates if the graph contains a cycle

    public TopologicalSort() {
        topoOrder = new ArrayList<>();
        hasCycle = false;
    }

    // Helper method to perform DFS and detect cycles
    private void topologicalSortUtil(String nodeId, Map<String, List<String>> adjList,
            Set<String> visited, Set<String> stack, Stack<String> resultStack) {
        // Mark the current node as visited and part of the recursion stack
        visited.add(nodeId);
        stack.add(nodeId);

        // Traverse all adjacent nodes
        for (String neighbor : adjList.getOrDefault(nodeId, new ArrayList<>())) {
            if (stack.contains(neighbor)) {
                hasCycle = true; // Cycle detected
                return;
            }
            if (!visited.contains(neighbor)) {
                topologicalSortUtil(neighbor, adjList, visited, stack, resultStack);
            }
        }

        // Backtrack: remove the node from the recursion stack
        stack.remove(nodeId);

        // Add the node to the result stack
        resultStack.push(nodeId);
    }

    // Method to perform Topological Sort
    public boolean performTopologicalSort(Graph graph) {
        Map<String, List<String>> adjList = new HashMap<>();
        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();
        Stack<String> resultStack = new Stack<>();

        // Build the adjacency list from the graph's edges
        for (Edge edge : graph.edges) {
            adjList.computeIfAbsent(edge.source, k -> new ArrayList<>()).add(edge.target);
        }

        // Perform DFS from all nodes
        for (Node node : graph.nodes) {
            if (!visited.contains(node.id)) {
                topologicalSortUtil(node.id, adjList, visited, stack, resultStack);
                if (hasCycle) {
                    return false; // Stop if a cycle is detected
                }
            }
        }

        // Convert stack to topological order
        topoOrder.clear();
        while (!resultStack.isEmpty()) {
            String nodeId = resultStack.pop();
            topoOrder.add(findNodeById(graph, nodeId));
        }

        return true;
    }

    // Helper method to find a node by ID in the graph
    private Node findNodeById(Graph graph, String id) {
        return graph.nodes.stream()
                .filter(node -> node.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    // Getter for the topological order
    public List<Node> getTopoOrder() {
        return topoOrder;
    }

    public boolean hasCycle() {
        return hasCycle;
    }
}
