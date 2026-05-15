package org.rysowaniegrafu.gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Wizualizacja Grafu - JIMP2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // wyśrodkowanie okna
        setLayout(new BorderLayout()); // układ: środek, lewo, prawo, góra, dół

        TopPanel topPanel = new TopPanel();
        add(topPanel, BorderLayout.NORTH);

        GraphCanvas canvas = new GraphCanvas();
        add(canvas, BorderLayout.CENTER);
    }
}
