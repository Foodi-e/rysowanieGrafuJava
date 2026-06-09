package org.rysowaniegrafu.gui;

import org.rysowaniegrafu.model.Edge;
import org.rysowaniegrafu.model.Graph;
import org.rysowaniegrafu.model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class GraphCanvas extends JPanel {

    private Graph graph;

    // Zmienne kamery
    private double panX = 0;
    private double panY = 0;
    private double zoomFactor = 1.0;

    public GraphCanvas() {
        setBackground(Color.WHITE);
        // Podpięcie kontrolera myszy
        new CanvasController(this);
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        // Reset kamery przy wczytaniu nowego grafu
        this.panX = 0;
        this.panY = 0;
        this.zoomFactor = 1.0;
        repaint();
    }

    public Graph getGraph() { return graph; }

    // Gettery i Settery dla kamery
    public double getPanX() { return panX; }
    public void setPanX(double panX) { this.panX = panX; }
    public double getPanY() { return panY; }
    public void setPanY(double panY) { this.panY = panY; }
    public double getZoomFactor() { return zoomFactor; }
    public void setZoomFactor(double zoomFactor) { this.zoomFactor = zoomFactor; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (graph == null) return;

        // --- retina ---
        AffineTransform oldAt = g2d.getTransform();

        // dodaje własne parametry kamery
        g2d.translate(panX, panY);
        g2d.scale(zoomFactor, zoomFactor);

        // Rysowanie krawędzi
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2.0f));

        if (graph.getEdges() != null) {
            for (Edge edge : graph.getEdges()) {
                Node n1 = edge.getU();
                Node n2 = edge.getV();
                g2d.drawLine((int) n1.getX(), (int) n1.getY(), (int) n2.getX(), (int) n2.getY());
            }
        }

        // Rysowanie wierzchołków
        int nodeRadius = 15;
        int nodeDiameter = nodeRadius * 2;

        if (graph.getNodes() != null) {
            for (Node node : graph.getNodes()) {
                int x = (int) node.getX() - nodeRadius;
                int y = (int) node.getY() - nodeRadius;

                // Wyróżnienie stałych węzłów (dla algorytmu Tutte'a)
                if (node.isFixed()) {
                    g2d.setColor(new Color(231, 76, 60)); // Czerwony
                } else {
                    g2d.setColor(new Color(52, 152, 219)); // Niebieski
                }

                g2d.fillOval(x, y, nodeDiameter, nodeDiameter);

                g2d.setColor(Color.DARK_GRAY);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawOval(x, y, nodeDiameter, nodeDiameter);

                // Rysowanie ID węzła nad nim
                g2d.drawString(String.valueOf(node.getId()), x + 5, y - 5);
            }
        }

        g2d.setTransform(oldAt);
    }
}