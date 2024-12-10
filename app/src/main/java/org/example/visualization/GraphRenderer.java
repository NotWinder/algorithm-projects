// GraphRenderer.java
package org.example.visualization;

import org.example.common.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import javax.imageio.ImageIO;

public class GraphRenderer {

    private Graph graph;
    private List<Node> topoOrder; // Topological order result

    public GraphRenderer(Graph graph) {
        this.graph = graph;
    }

    public void setTopoOrder(List<Node> topoOrder) {
        this.topoOrder = topoOrder;
    }

    // Method to render the graph with arrows on edges
    public void renderGraph(String fileName, boolean showBFS, boolean showDFS,
            Set<Edge> bfsPathEdges, Set<Edge> dfsPathEdges,
            java.util.List<Node> bfsOrder, java.util.List<Node> dfsOrder) throws IOException {
        double minX = graph.nodes.stream().mapToDouble(n -> n.x).min().orElse(0);
        double maxX = graph.nodes.stream().mapToDouble(n -> n.x).max().orElse(0);
        double minY = graph.nodes.stream().mapToDouble(n -> n.y).min().orElse(0);
        double maxY = graph.nodes.stream().mapToDouble(n -> n.y).max().orElse(0);

        int width = 1920;
        int height = 1080;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable antialiasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the stroke (thickness) of the edges
        int edgeThickness = 3; // Increase this value for thicker edges
        g2d.setStroke(new BasicStroke(edgeThickness));

        // Draw edges with arrows
        for (Edge edge : graph.edges) {
            Node source = findNodeById(edge.source);
            Node target = findNodeById(edge.target);

            if (source != null && target != null) {
                int x1 = scaleX(source.x, minX, maxX, width);
                int y1 = scaleY(source.y, minY, maxY, height);
                int x2 = scaleX(target.x, minX, maxX, width);
                int y2 = scaleY(target.y, minY, maxY, height);

                // Calculate the angle between the source and target nodes
                double angle = Math.atan2(y2 - y1, x2 - x1);

                // Apply a small offset from the node's center
                int offset = 20; // Adjust this value as needed
                int adjustedX1 = (int) (x1 + offset * Math.cos(angle));
                int adjustedY1 = (int) (y1 + offset * Math.sin(angle));
                int adjustedX2 = (int) (x2 - offset * Math.cos(angle));
                int adjustedY2 = (int) (y2 - offset * Math.sin(angle));

                // Set the color of the edge based on BFS/DFS conditions
                if (showBFS && bfsPathEdges.contains(edge)) {
                    g2d.setColor(Color.GREEN);
                } else if (showDFS && dfsPathEdges.contains(edge)) {
                    g2d.setColor(Color.RED);
                } else {
                    g2d.setColor(Color.GRAY);
                }

                // Draw the line with adjusted positions (bolder line)
                g2d.draw(new Line2D.Double(adjustedX1, adjustedY1, adjustedX2, adjustedY2));

                // Draw the arrowhead at the end of the line
                drawArrowhead(g2d, adjustedX1, adjustedY1, adjustedX2, adjustedY2);
            }
        }

        // Draw nodes
        for (Node node : graph.nodes) {
            int x = scaleX(node.x, minX, maxX, width);
            int y = scaleY(node.y, minY, maxY, height);
            int nodeSize = 10;

            // Assign color based on algorithm
            if (showBFS && bfsOrder.contains(node)) {
                g2d.setColor(Color.ORANGE);
            } else if (showDFS && dfsOrder.contains(node)) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.BLUE);
            }

            g2d.fill(new Ellipse2D.Double(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize));
            g2d.setColor(Color.BLACK);
            g2d.drawString(node.id, x + 5, y - 5);
        }

        // Write the image to a file
        File outputFile = new File(fileName);
        ImageIO.write(image, "PNG", outputFile);

        g2d.dispose();
    }

    // Helper method to draw arrowheads
    private void drawArrowhead(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        final int ARROW_SIZE = 10;
        // Calculate the angle of the edge
        double angle = Math.atan2(y2 - y1, x2 - x1);
        // Calculate the coordinates of the arrowhead
        int x3 = (int) (x2 - ARROW_SIZE * Math.cos(angle - Math.PI / 6));
        int y3 = (int) (y2 - ARROW_SIZE * Math.sin(angle - Math.PI / 6));
        int x4 = (int) (x2 - ARROW_SIZE * Math.cos(angle + Math.PI / 6));
        int y4 = (int) (y2 - ARROW_SIZE * Math.sin(angle + Math.PI / 6));

        // Draw the arrowhead
        g2d.setColor(Color.BLACK);
        g2d.drawLine(x2, y2, x3, y3);
        g2d.drawLine(x2, y2, x4, y4);
    }

    // Renders the topological order as an image
    public void renderTopologicalSort(Graph graph, String fileName) throws IOException {
        if (topoOrder == null || topoOrder.isEmpty()) {
            System.out.println("Topological order is empty or null. Cannot render.");
            return;
        }

        int width = 6000;
        int height = 200;
        int nodeSize = 50;
        int spacing = 150;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable antialiasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the background color
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Draw nodes in topological order
        for (int i = 0; i < topoOrder.size(); i++) {
            Node node = topoOrder.get(i);
            int x = spacing * (i + 1);
            int y = height / 2;

            // Draw the node
            g2d.setColor(Color.BLUE);
            g2d.fill(new Ellipse2D.Double(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize));

            // Draw the node ID
            g2d.setColor(Color.WHITE);
            g2d.drawString(node.id, x - nodeSize / 4, y + 5);

            // Draw an edge to the next node in the order
            if (i < topoOrder.size() - 1) {
                int nextX = spacing * (i + 2);
                g2d.setColor(Color.BLACK);
                g2d.drawLine(x, y, nextX, y);
            }
        }

        System.out.println("Rendering topological order: " + topoOrder); // Debugging line

        // Save the image to a file
        File outputFile = new File(fileName);
        ImageIO.write(image, "PNG", outputFile);

        g2d.dispose();
    }

    // Renders the Kosaraju's algorithm result as an image
    public void renderKosaraju(String fileName) throws IOException {
        int width = 1920;
        int height = 1080;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable antialiasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the stroke (thickness) of the edges
        int edgeThickness = 3; // Increase this value for thicker edges
        g2d.setStroke(new BasicStroke(edgeThickness));

        // Draw edges with arrows
        for (Edge edge : graph.edges) {
            Node source = null;
            Node target = null;
            for (Node node : graph.nodes) {
                if (node.id.equals(edge.source)) {
                    source = node;
                }
                if (node.id.equals(edge.target)) {
                    target = node;
                }
            }

            if (source != null && target != null) {
                int x1 = scaleX(source.x, graph.nodes.stream().mapToDouble(n -> n.x).min().orElse(0),
                        graph.nodes.stream().mapToDouble(n -> n.x).max().orElse(0), width);
                int y1 = scaleY(source.y, graph.nodes.stream().mapToDouble(n -> n.y).min().orElse(0),
                        graph.nodes.stream().mapToDouble(n -> n.y).max().orElse(0), height);
                int x2 = scaleX(target.x, graph.nodes.stream().mapToDouble(n -> n.x).min().orElse(0),
                        graph.nodes.stream().mapToDouble(n -> n.x).max().orElse(0), width);
                int y2 = scaleY(target.y, graph.nodes.stream().mapToDouble(n -> n.y).min().orElse(0),
                        graph.nodes.stream().mapToDouble(n -> n.y).max().orElse(0), height);

                // Calculate the angle between the source and target nodes
                double angle = Math.atan2(y2 - y1, x2 - x1);

                // Apply a small offset from the node's center
                int offset = 20; // Adjust this value as needed
                int adjustedX1 = (int) (x1 + offset * Math.cos(angle));
                int adjustedY1 = (int) (y1 + offset * Math.sin(angle));
                int adjustedX2 = (int) (x2 - offset * Math.cos(angle));
                int adjustedY2 = (int) (y2 - offset * Math.sin(angle));

                // Draw the edge
                g2d.drawLine(x1, y1, x2, y2);

                // Draw the arrowhead
                drawArrowhead(g2d, adjustedX1, adjustedY1, adjustedX2, adjustedY2);
            }
        }

        // Save the image to a file
        File outputFile = new File(fileName);
        ImageIO.write(image, "PNG", outputFile);
    }

    // Draws the graph with nodes and edges
    public void drawGraph(Graphics2D g2d, int width, int height) {
        // Enable antialiasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the stroke (thickness) of the edges
        int edgeThickness = 3; // Increase this value for thicker edges
        g2d.setStroke(new BasicStroke(edgeThickness));

        // Draw edges with arrows
        for (Edge edge : graph.edges) {
            Node source = null;
            Node target = null;
            for (Node node : graph.nodes) {
                if (node.id.equals(edge.source)) {
                    source = node;
                }
                if (node.id.equals(edge.target)) {
                    target = node;
                }
            }

            if (source != null && target != null) {
                int x1 = scaleX(source.x, graph.nodes.stream().mapToDouble(n -> n.x).min().orElse(0),
                        graph.nodes.stream().mapToDouble(n -> n.x).max().orElse(0), width);
                int y1 = scaleY(source.y, graph.nodes.stream().mapToDouble(n -> n.y).min().orElse(0),
                        graph.nodes.stream().mapToDouble(n -> n.y).max().orElse(0), height);
                int x2 = scaleX(target.x, graph.nodes.stream().mapToDouble(n -> n.x).min().orElse(0),
                        graph.nodes.stream().mapToDouble(n -> n.x).max().orElse(0), width);
                int y2 = scaleY(target.y, graph.nodes.stream().mapToDouble(n -> n.y).min().orElse(0),
                        graph.nodes.stream().mapToDouble(n -> n.y).max().orElse(0), height);

                // Calculate the angle between the source and target nodes
                double angle = Math.atan2(y2 - y1, x2 - x1);

                // Apply a small offset from the node's center
                int offset = 20; // Adjust this value as needed
                int adjustedX1 = (int) (x1 + offset * Math.cos(angle));
                int adjustedY1 = (int) (y1 + offset * Math.sin(angle));
                int adjustedX2 = (int) (x2 - offset * Math.cos(angle));
                int adjustedY2 = (int) (y2 - offset * Math.sin(angle));

                // Draw the edge
                g2d.drawLine(x1, y1, x2, y2);

                // Draw the arrowhead
                drawArrowhead(g2d, adjustedX1, adjustedY1, adjustedX2, adjustedY2);
            }
        }

        // Draw nodes
        for (Node node : graph.nodes) {
            int x = scaleX(node.x, graph.nodes.stream().mapToDouble(n -> n.x).min().orElse(0),
                    graph.nodes.stream().mapToDouble(n -> n.x).max().orElse(0), width);
            int y = scaleY(node.y, graph.nodes.stream().mapToDouble(n -> n.y).min().orElse(0),
                    graph.nodes.stream().mapToDouble(n -> n.y).max().orElse(0), height);

            // Draw the node
            g2d.setColor(Color.BLUE);
            g2d.fill(new Ellipse2D.Double(x - 20, y - 20, 40, 40));

            // Draw the node ID
            g2d.setColor(Color.WHITE);
            g2d.drawString(node.id, x - 10, y + 5);
        }
    }

    private int scaleX(double x, double minX, double maxX, int width) {
        return (int) (((x - minX) / (maxX - minX)) * (width * 0.8) + width * 0.1);
    }

    private int scaleY(double y, double minY, double maxY, int height) {
        return (int) (((y - minY) / (maxY - minY)) * (height * 0.8) + height * 0.1);
    }

    private Node findNodeById(String id) {
        return graph.nodes.stream()
                .filter(node -> node.id.equals(id))
                .findFirst()
                .orElse(null);
    }
}
