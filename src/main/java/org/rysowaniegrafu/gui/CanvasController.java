package org.rysowaniegrafu.gui;

import org.rysowaniegrafu.model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class CanvasController extends MouseAdapter {

    private final GraphCanvas canvas;
    private Point lastMousePos;

    // Zmienna przechowująca wierzchołek, który aktualnie trzymamy myszką
    private Node draggedNode = null;

    public CanvasController(GraphCanvas canvas) {
        this.canvas = canvas;
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addMouseWheelListener(this);
    }

    // --- OBSŁUGA KLIKNIĘĆ (Edycja i Łapanie Węzła) ---
    @Override
    public void mousePressed(MouseEvent e) {
        lastMousePos = e.getPoint();
        Node clickedNode = findNodeAt(e.getX(), e.getY());

        if (e.getClickCount() == 2) {
            // PODWÓJNY KLIK -> Otwieramy okienko (zgodnie ze specyfikacją)
            if (clickedNode != null) {
                Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(canvas);
                NodeEditDialog dialog = new NodeEditDialog(parentFrame, clickedNode, canvas);
                dialog.setVisible(true);
            }
        } else if (e.getClickCount() == 1) {
            // POJEDYNCZY KLIK -> Sprawdzamy, czy złapaliśmy węzeł do przeciągania
            if (clickedNode != null) {
                draggedNode = clickedNode;
            }
        }
    }

    // --- ZWOLNIENIE MYSZKI (Puszczenie Węzła) ---
    @Override
    public void mouseReleased(MouseEvent e) {
        // Niezależnie co trzymaliśmy, puszczamy to
        draggedNode = null;
    }

    // --- OBSŁUGA PRZESUWANIA (Pan Kamery ATA Przeciąganie Węzła) ---
    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedNode != null) {
            // WARIANT 1: Przeciągamy wierzchołek
            try {
                // Musimy przeliczyć pozycję kursora na ekranie z powrotem na współrzędne w świecie grafu
                AffineTransform at = new AffineTransform();
                at.translate(canvas.getPanX(), canvas.getPanY());
                at.scale(canvas.getZoomFactor(), canvas.getZoomFactor());

                Point2D.Float screenPoint = new Point2D.Float(e.getX(), e.getY());
                Point2D worldPoint = at.inverseTransform(screenPoint, null);

                // Aktualizujemy pozycję trzymanego węzła
                draggedNode.setX(worldPoint.getX());
                draggedNode.setY(worldPoint.getY());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // WARIANT 2: Przesuwamy całą kamerę (Pan)
            int dx = e.getX() - lastMousePos.x;
            int dy = e.getY() - lastMousePos.y;

            canvas.setPanX(canvas.getPanX() + dx);
            canvas.setPanY(canvas.getPanY() + dy);
            lastMousePos = e.getPoint();
        }

        // Zawsze odświeżamy ekran, żeby widzieć ruch na żywo
        canvas.repaint();
    }

    // --- OBSŁUGA ROLKI (Zoom) ---
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double scaleFactor = 1.1;
        if (e.getWheelRotation() > 0) {
            canvas.setZoomFactor(canvas.getZoomFactor() / scaleFactor); // Oddalanie
        } else {
            canvas.setZoomFactor(canvas.getZoomFactor() * scaleFactor); // Przybliżanie
        }
        canvas.repaint();
    }

    // --- MATEMATYKA: Wyszukiwanie węzła uwzględniając kamerę ---
    private Node findNodeAt(int screenX, int screenY) {
        if (canvas.getGraph() == null || canvas.getGraph().getNodes() == null) return null;

        try {
            AffineTransform at = new AffineTransform();
            at.translate(canvas.getPanX(), canvas.getPanY());
            at.scale(canvas.getZoomFactor(), canvas.getZoomFactor());

            Point2D.Float screenPoint = new Point2D.Float(screenX, screenY);
            Point2D worldPoint = at.inverseTransform(screenPoint, null);

            int hitboxRadius = 25;

            for (Node node : canvas.getGraph().getNodes()) {
                double dx = node.getX() - worldPoint.getX();
                double dy = node.getY() - worldPoint.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= hitboxRadius) {
                    return node;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}