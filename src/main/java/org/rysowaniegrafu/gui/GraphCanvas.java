package org.rysowaniegrafu.gui;

import org.rysowaniegrafu.model.Edge;
import org.rysowaniegrafu.model.Graph;
import org.rysowaniegrafu.model.Node;

import javax.swing.*;
import java.awt.*;

public class GraphCanvas extends JPanel {

    private Graph graph;

    public GraphCanvas() {
        setBackground(Color.WHITE);
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        repaint(); // wywołuje paintComponent(...)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // wyczyszczanie tła

        Graphics2D g2d = (Graphics2D) g;

        // wygładzanie krawędzi
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (graph == null) return;

        // rysowanie krawędzi
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(2.0f)); // grubość linii

        for (Edge edge : graph.getEdges()) {
            Node n1 = edge.getU();
            Node n2 = edge.getV();
            g2d.drawLine((int) n1.getX(), (int) n1.getY(), (int) n2.getX(), (int) n2.getY());
        }

        // rysowanie wierzchołków
        int nodeRadius = 15;
        int nodeDiameter = nodeRadius * 2;

        for (Node node : graph.getNodes()) {
            // współrzędne w Javie to lewy górny róg obiektu, więc musimy przesunąć środek o promień
            int x = (int) node.getX() - nodeRadius;
            int y = (int) node.getY() - nodeRadius;

            g2d.setColor(new Color(52, 152, 219)); // niebieski
            g2d.fillOval(x, y, nodeDiameter, nodeDiameter);

            // obramowanie wierzchołka
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(x, y, nodeDiameter, nodeDiameter);
        }
    }
}
