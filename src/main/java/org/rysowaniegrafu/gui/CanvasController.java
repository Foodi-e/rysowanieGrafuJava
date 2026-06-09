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

    // edycja i łapanie Węzła
    @Override
    public void mousePressed(MouseEvent e) {
        lastMousePos = e.getPoint();
        Node clickedNode = findNodeAt(e.getX(), e.getY());

        if (e.getClickCount() == 2) {
            // 2 klik -> Otwieramy okienko
            if (clickedNode != null) {
                Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(canvas);
                NodeEditDialog dialog = new NodeEditDialog(parentFrame, clickedNode, canvas);
                dialog.setVisible(true);
            }
        } else if (e.getClickCount() == 1) {
            // 1 klik -> Sprawdzanie czy złapaliśmy węzeł do przeciągania
            if (clickedNode != null) {
                draggedNode = clickedNode;
            }
        }
    }

    // Puszczenie Węzła
    @Override
    public void mouseReleased(MouseEvent e) {
        draggedNode = null;
    }

    // przesuwanie
    @Override
    public void mouseDragged(MouseEvent e) {
        if (draggedNode != null) {
            // Przeciągamy wierzchołek
            try {
                //  przeliczanie pozycję kursora na ekranie z powrotem na współrzędne w grafu
                AffineTransform at = new AffineTransform();
                at.translate(canvas.getPanX(), canvas.getPanY());
                at.scale(canvas.getZoomFactor(), canvas.getZoomFactor());

                Point2D.Float screenPoint = new Point2D.Float(e.getX(), e.getY());
                Point2D worldPoint = at.inverseTransform(screenPoint, null);

                // Aktualizacja pozycji trzymanego węzła
                draggedNode.setX(worldPoint.getX());
                draggedNode.setY(worldPoint.getY());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // Przesuwanie  kamery
            int dx = e.getX() - lastMousePos.x;
            int dy = e.getY() - lastMousePos.y;

            canvas.setPanX(canvas.getPanX() + dx);
            canvas.setPanY(canvas.getPanY() + dy);
            lastMousePos = e.getPoint();
        }

        // odświeżanie ekran
        canvas.repaint();
    }

    // Zoom
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

    // wyszukiwanie węzła uwzględniając kamerę
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