package org.rysowaniegrafu.gui;

import org.rysowaniegrafu.model.Node;

import javax.swing.*;
import java.awt.*;

public class NodeEditDialog extends JDialog {

    public NodeEditDialog(Frame owner, Node node, GraphCanvas canvas) {
        super(owner, "Edycja Wierzchołka ID: " + node.getId(), true);
        setLayout(new GridLayout(3, 2, 10, 10));
        setSize(250, 150);
        setLocationRelativeTo(owner);

        JLabel lblX = new JLabel("Pozycja X:");
        JTextField txtX = new JTextField(String.valueOf(node.getX()));
        JLabel lblY = new JLabel("Pozycja Y:");
        JTextField txtY = new JTextField(String.valueOf(node.getY()));

        JButton btnSave = new JButton("Zapisz");
        JButton btnCancel = new JButton("Anuluj");

        btnSave.addActionListener(e -> {
            try {
                double newX = Double.parseDouble(txtX.getText());
                double newY = Double.parseDouble(txtY.getText());
                node.setX(newX);
                node.setY(newY);
                canvas.repaint();
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Wprowadź poprawne wartości liczbowe!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dispose());

        add(lblX); add(txtX);
        add(lblY); add(txtY);
        add(btnSave); add(btnCancel);
    }
}