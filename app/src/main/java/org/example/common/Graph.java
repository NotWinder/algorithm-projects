// Graph.java
package org.example.common;

import java.util.*;

public class Graph {
    public List<Node> nodes;
    public List<Edge> edges;

    // Constructor
    public Graph(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    // Check if the graph is planar
    public boolean isPlanar() {
        // Planar graphs must not contain K5 or K3,3 subgraphs
        return !containsK5() && !containsK33();
    }

    // Check if the graph contains a K5 subgraph
    private boolean containsK5() {
        // K5 has 5 vertices and 10 edges. Check for this condition
        if (nodes.size() < 5)
            return false;
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                Set<Node> commonNeighbors = new HashSet<>();
                for (Edge edge : edges) {
                    if ((edge.source.equals(nodes.get(i).id) && edge.target.equals(nodes.get(j).id)) ||
                            (edge.source.equals(nodes.get(j).id) && edge.target.equals(nodes.get(i).id))) {
                        commonNeighbors.add(nodes.get(i));
                        commonNeighbors.add(nodes.get(j));
                    }
                }
                if (commonNeighbors.size() >= 5) {
                    return true; // Found a K5 subgraph
                }
            }
        }
        return false;
    }

    // Check if the graph contains a K3,3 subgraph
    private boolean containsK33() {
        // For K3,3, the graph needs to be bipartite with two sets of 3 nodes each, and
        // all vertices in set 1 must be connected to all vertices in set 2
        return false; // Placeholder, implement bipartite detection and check for K3,3 subgraph
    }

    // Utility to print the graph edges
    public void printGraph() {
        for (Edge edge : edges) {
            System.out.println(edge.source + " - " + edge.target);
        }
    }
}
