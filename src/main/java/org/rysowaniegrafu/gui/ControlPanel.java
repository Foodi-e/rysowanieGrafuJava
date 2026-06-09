package org.rysowaniegrafu.gui;

import org.rysowaniegrafu.model.Algorithm;
import org.rysowaniegrafu.model.Config;
import org.rysowaniegrafu.model.SelectionMode;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    private final Config config;
    private Runnable onConfigChanged;

    private JPanel panelTutte;
    private JPanel panelFR;

    public ControlPanel(Config config) {
        this.config = config;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Parametry Algorytmu"));

        // szerokość panelu
        setPreferredSize(new Dimension(260, 0));

        createTuttePanel();
        createFRPanel();

        add(panelTutte);
        add(panelFR);

        setAlgorithm(config.getAlgorithm());
    }

    private void createTuttePanel() {
        panelTutte = new JPanel();
        panelTutte.setLayout(new BoxLayout(panelTutte, BoxLayout.Y_AXIS));
        panelTutte.setBorder(BorderFactory.createTitledBorder("Opcje: Tutte"));

        // Promień
        JLabel lblRadius = new JLabel("Promień okręgu:");
        lblRadius.setAlignmentX(Component.LEFT_ALIGNMENT); // TWARDE WYRÓWNANIE DO LEWEJ
        panelTutte.add(lblRadius);

        JSpinner spinRadius = new JSpinner(new SpinnerNumberModel(config.getRadius(), 50.0, 2000.0, 10.0));
        spinRadius.setMaximumSize(new Dimension(230, 30));
        spinRadius.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinRadius.addChangeListener(e -> {
            config.setRadius((Double) spinRadius.getValue());
            triggerChange();
        });
        panelTutte.add(spinRadius);
        panelTutte.add(Box.createVerticalStrut(10));

        // Liczba stałych węzłów (kFixed)
        JLabel lblKFixed = new JLabel("Stałe węzły (ramka):");
        lblKFixed.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTutte.add(lblKFixed);

        JSpinner spinKFixed = new JSpinner(new SpinnerNumberModel(config.getKFixed(), 3, 100, 1));
        spinKFixed.setMaximumSize(new Dimension(230, 30));
        spinKFixed.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinKFixed.addChangeListener(e -> {
            config.setKFixed((Integer) spinKFixed.getValue());
            triggerChange();
        });
        panelTutte.add(spinKFixed);
        panelTutte.add(Box.createVerticalStrut(10));

        // Tryb wyboru wierzchołków
        JLabel lblMode = new JLabel("Wybór wierzchołków ramki:");
        lblMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTutte.add(lblMode);

        JComboBox<SelectionMode> comboMode = new JComboBox<>(SelectionMode.values());
        comboMode.setSelectedItem(config.getSelectionMode());
        comboMode.setMaximumSize(new Dimension(230, 30));
        comboMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboMode.addActionListener(e -> {
            config.setSelectionMode((SelectionMode) comboMode.getSelectedItem());
            triggerChange();
        });
        panelTutte.add(comboMode);
    }

    private void createFRPanel() {
        panelFR = new JPanel();
        panelFR.setLayout(new BoxLayout(panelFR, BoxLayout.Y_AXIS));
        panelFR.setBorder(BorderFactory.createTitledBorder("Opcje: Fruchterman-R."));

        // Temperatura
        JLabel lblTemp = new JLabel("Temperatura początkowa:");
        lblTemp.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFR.add(lblTemp);

        JSpinner spinTemp = new JSpinner(new SpinnerNumberModel(config.getTemp(), 1.0, 1000.0, 5.0));
        spinTemp.setMaximumSize(new Dimension(230, 30));
        spinTemp.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinTemp.addChangeListener(e -> {
            config.setTemp((Double) spinTemp.getValue());
            triggerChange();
        });
        panelFR.add(spinTemp);
        panelFR.add(Box.createVerticalStrut(10));

        // Liczba iteracji
        JLabel lblIter = new JLabel("Liczba iteracji:");
        lblIter.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFR.add(lblIter);

        JSpinner spinIter = new JSpinner(new SpinnerNumberModel(config.getIterations(), 10, 10000, 50));
        spinIter.setMaximumSize(new Dimension(230, 30));
        spinIter.setAlignmentX(Component.LEFT_ALIGNMENT);
        spinIter.addChangeListener(e -> {
            config.setIterations((Integer) spinIter.getValue());
            triggerChange();
        });
        panelFR.add(spinIter);
    }

    public void setAlgorithm(Algorithm algo) {
        if (algo == Algorithm.TUTTE) {
            panelTutte.setVisible(true);
            panelFR.setVisible(false);
        } else if (algo == Algorithm.FRUCHTERMAN) {
            panelTutte.setVisible(false);
            panelFR.setVisible(true);
        }
        revalidate();
        repaint();
    }

    public void setOnConfigChanged(Runnable onConfigChanged) {
        this.onConfigChanged = onConfigChanged;
    }

    private void triggerChange() {
        if (onConfigChanged != null) {
            onConfigChanged.run();
        }
    }
}