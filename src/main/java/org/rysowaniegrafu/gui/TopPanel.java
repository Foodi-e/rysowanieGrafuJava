package org.rysowaniegrafu.gui;

import org.rysowaniegrafu.io.DataReader;
import org.rysowaniegrafu.io.MockDataLoader;
import org.rysowaniegrafu.model.Algorithm;
import org.rysowaniegrafu.model.Graph;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

public class TopPanel extends JPanel {

    private JButton btnLoad;
    private JButton btnSave;
    private JComboBox<Algorithm> comboAlgorithm;
    private JButton btnStart;

    private Consumer<Graph> onGraphLoaded;

    private Consumer<Algorithm> onStartClicked;
    public void setOnStartClicked(Consumer<Algorithm> onStartClicked) {
        this.onStartClicked = onStartClicked;
    }

    private Consumer<Algorithm> onAlgorithmChanged;
    public void setOnAlgorithmChanged(Consumer<Algorithm> onAlgorithmChanged) {
        this.onAlgorithmChanged = onAlgorithmChanged;
    }

    public TopPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        // Inicjalizacja przycisków
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

    public void setOnGraphLoaded(Consumer<Graph> onGraphLoaded) {
        this.onGraphLoaded = onGraphLoaded;
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

                try {
                    Graph loadedGraph = DataReader.LoadOnlyEdges(fileToLoad.getAbsolutePath());

                    if (onGraphLoaded != null) {
                        onGraphLoaded.accept(loadedGraph);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Błąd wczytywania pliku: " + ex.getMessage(), "Błąd I/O", JOptionPane.ERROR_MESSAGE);
                }
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

        btnStart.addActionListener(e -> {
            if (onStartClicked != null) {
                // Pobieramy algorytm aktualnie wybrany z listy rozwijanej
                Algorithm selectedAlgo = (Algorithm) comboAlgorithm.getSelectedItem();
                onStartClicked.accept(selectedAlgo);
            }
        });

        comboAlgorithm.addActionListener(e -> {
            if (onAlgorithmChanged != null) {
                onAlgorithmChanged.accept((Algorithm) comboAlgorithm.getSelectedItem());
            }
        });
    }
}