// Edge.java
package org.example.common;

import java.util.Objects;

public class Edge {
    public String source;
    public String target;

    // Constructor to create an edge from source to target
    public Edge(String source, String target) {
        this.source = source;
        this.target = target;
    }

    // Overriding equals and hashCode for proper Set operations
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Edge edge = (Edge) obj;
        return Objects.equals(source, edge.source) && Objects.equals(target, edge.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
