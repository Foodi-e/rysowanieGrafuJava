package org.rysowaniegrafu.gui;

import org.rysowaniegrafu.model.Algorithm;
import org.rysowaniegrafu.model.Graph;
import org.rysowaniegrafu.io.DataReader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TopPanel extends JPanel {

    private JButton btnLoad;
    private JButton btnSave;
    private JComboBox<Algorithm> comboAlgorithm;
    private JButton btnStart;

    private Consumer<Graph> onGraphLoaded;
    private Consumer<Algorithm> onAlgorithmChanged;
    private Consumer<Algorithm> onStartClicked;

    private BiConsumer<String, Boolean> onSaveClicked;

    public void setOnSaveClicked(BiConsumer<String, Boolean> onSaveClicked) {
        this.onSaveClicked = onSaveClicked;
    }

    public TopPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        btnLoad = new JButton("Wczytaj Graf");
        btnSave = new JButton("Zapisz Wynik");

        comboAlgorithm = new JComboBox<>(Algorithm.values());

        btnStart = new JButton("START");
        btnStart.setBackground(new Color(46, 204, 113));
        btnStart.setOpaque(true);
        btnStart.setBorderPainted(false);

        setupActions();

        add(btnLoad);
        add(btnSave);
        add(new JLabel("Algorytm:"));
        add(comboAlgorithm);
        add(btnStart);
    }

    public void setOnGraphLoaded(Consumer<Graph> onGraphLoaded) { this.onGraphLoaded = onGraphLoaded; }
    public void setOnAlgorithmChanged(Consumer<Algorithm> onAlgorithmChanged) { this.onAlgorithmChanged = onAlgorithmChanged; }
    public void setOnStartClicked(Consumer<Algorithm> onStartClicked) { this.onStartClicked = onStartClicked; }

    private void setupActions() {
        // wczytywanie
        btnLoad.addActionListener(e -> {
            String[] options = {"Tylko krawędzie (TXT)", "Współrzędne + Krawędzie (CSV)", "Pliki Binarne (BIN)"};
            int choice = JOptionPane.showOptionDialog(this,
                    "Wybierz tryb wczytywania danych:",
                    "Wczytywanie Grafu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);

            try {
                if (choice == 0) {
                    // Tylko krawędzie
                    File f = chooseFile("Wybierz plik z listą krawędzi (.txt)");
                    if (f != null) {
                        Graph g = DataReader.LoadOnlyEdges(f.getAbsolutePath());
                        if (onGraphLoaded != null) onGraphLoaded.accept(g);
                    }
                } else if (choice == 1) {
                    // Tekstowe współrzędne (CSV)
                    File fn = chooseFile("KROK 1: Wybierz plik z WĘZŁAMI (.csv)");
                    if (fn != null) {
                        File fe = chooseFile("KROK 2: Wybierz plik z KRAWĘDZIAMI (.txt/.csv)");
                        if (fe != null) {
                            Graph g = DataReader.LoadCSV(fn.getAbsolutePath(), fe.getAbsolutePath());
                            if (onGraphLoaded != null) onGraphLoaded.accept(g);
                        }
                    }
                } else if (choice == 2) {
                    // Pliki Binarne
                    File fn = chooseFile("KROK 1: Wybierz plik BINARNY z WĘZŁAMI (.bin)");
                    if (fn != null) {
                        File fe = chooseFile("KROK 2: Wybierz plik z KRAWĘDZIAMI");
                        if (fe != null) {
                            Graph g = DataReader.LoadBin(fn.getAbsolutePath(), fe.getAbsolutePath());
                            if (onGraphLoaded != null) onGraphLoaded.accept(g);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Błąd podczas wczytywania plików:\n" + ex.getMessage(), "Błąd I/O", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Obsługa wyboru algorytmu z listy
        comboAlgorithm.addActionListener(e -> {
            if (onAlgorithmChanged != null) {
                onAlgorithmChanged.accept((Algorithm) comboAlgorithm.getSelectedItem());
            }
        });

        // Obsługa STARTu
        btnStart.addActionListener(e -> {
            if (onStartClicked != null) {
                onStartClicked.accept((Algorithm) comboAlgorithm.getSelectedItem());
            }
        });

        // Zapis wyniku
        btnSave.addActionListener(e -> {
            String[] options = {"Tekstowy (CSV)", "Binarny (BIN)"};
            int choice = JOptionPane.showOptionDialog(this,
                    "Wybierz format zapisu wyniku:",
                    "Zapisywanie Wyników",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);

            if (choice == JOptionPane.CLOSED_OPTION) return;

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Wybierz miejsce zapisu wyniku");

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String path = fileToSave.getAbsolutePath();
                boolean isBinary = (choice == 1);

                // Przekazujemy żądanie zapisu w górę do MainFrame
                if (onSaveClicked != null) {
                    onSaveClicked.accept(path, isBinary);
                }
            }
        });
    }

    // Metoda pomocnicza
    private File chooseFile(String title) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(title);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }
}