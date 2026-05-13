package org.rysowaniegrafu.gui;

import org.rysowaniegrafu.algorithms.Fruchterman;
import org.rysowaniegrafu.model.Algorithm;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TopPanel extends JPanel {

    private JButton btnLoad;
    private JButton btnSave;
    private JComboBox<Algorithm> comboAlgorithm;
    private JButton btnStart;

    public TopPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        // iInicjalizacja przycisków
        btnLoad = new JButton("Wczytaj Graf");
        btnSave = new JButton("Zapisz Wynik");

        comboAlgorithm = new JComboBox<>(Algorithm.values());

        btnStart = new JButton("START");
        btnStart.setBackground(new Color(46, 204, 113)); // zielony
        btnStart.setOpaque(true);
        btnStart.setBorderPainted(false);

        setupActions();
        add(btnLoad);
        add(btnSave);
        add(new JLabel("Algorytm:"));
        add(comboAlgorithm);
        add(btnStart);
    }

    private void setupActions() {
        // wczytywanie pliku
        btnLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Wybierz plik z grafem");

            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                System.out.println("Wybrano plik do wczytania: " + fileToLoad.getAbsolutePath());
                //TODO: dodac data reader
            }
        });

        btnSave.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Wybierz miejsce zapisu wyniku");

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                System.out.println("Wybrano miejsce do zapisu: " + fileToSave.getAbsolutePath());
                //TODO: dodać data writer
            }
        });
    }
}