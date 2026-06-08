package org.rysowaniegrafu.gui;

import org.rysowaniegrafu.model.Algorithm;
import org.rysowaniegrafu.model.Config;
import org.rysowaniegrafu.model.Graph;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // Globalny stan aplikacji
    private final Config config = new Config();
    private Algorithm lastRunAlgorithm = null;

    public MainFrame() {
        setTitle("Wizualizacja Grafu - JIMP2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        TopPanel topPanel = new TopPanel();
        GraphCanvas canvas = new GraphCanvas();
        ControlPanel controlPanel = new ControlPanel(config); // Inicjalizacja nowego panelu

        // --- 1. ODBIÓR GRAFU (Wczytywanie) ---
        topPanel.setOnGraphLoaded(graph -> {
            canvas.setGraph(graph);
            canvas.setPanX(canvas.getWidth() / 2.0);
            canvas.setPanY(canvas.getHeight() / 2.0);
            canvas.setZoomFactor(1.0);
            lastRunAlgorithm = null; // Resetujemy stan
            canvas.repaint();
        });

        // --- 2. AKCJA PRZYCISKU START ---
        topPanel.setOnStartClicked(algorithm -> {
            if (canvas.getGraph() == null || canvas.getGraph().getNodes().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Najpierw wczytaj graf!", "Brak Danych", JOptionPane.WARNING_MESSAGE);
                return;
            }
            lastRunAlgorithm = algorithm;
            runCalculations(canvas);

            // Auto-Focus przy "twardym" starcie
            canvas.setPanX(canvas.getWidth() / 2.0);
            canvas.setPanY(canvas.getHeight() / 2.0);
            canvas.repaint();
        });

        // --- 3. AKCJA ZMIANY PARAMETRU W CZASIE RZECZYWISTYM ---
        controlPanel.setOnConfigChanged(() -> {
            if (lastRunAlgorithm != null) {
                runCalculations(canvas); // Przeliczamy ponownie fizykę
                canvas.repaint();        // Rysujemy natychmiast bez resetowania pozycji kamery
            }
        });

        // --- 4. AKCJA ZMIANY ALGORYTMU NA LIŚCIE ---
        topPanel.setOnAlgorithmChanged(algorithm -> {
            config.setAlgorithm(algorithm); // Aktualizujemy główny Config
            controlPanel.setAlgorithm(algorithm); // Wymuszamy zmianę widoku suwaków!
        });

        // --- 5. OBSŁUGA ZAPISU GRAFU ---
        topPanel.setOnSaveClicked((path, isBinary) -> {
            Graph currentGraph = canvas.getGraph();

            if (currentGraph == null || currentGraph.getNodes().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nie ma żadnego grafu do zapisania!", "Błąd zapisu", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                if (isBinary) {
                    System.out.println("Zapis struktury binarnej BIN do: " + path);
                    org.rysowaniegrafu.io.DataWriter.SaveBin(path, currentGraph);
                } else {
                    System.out.println("Zapis struktury tekstowej CSV do: " + path);
                    org.rysowaniegrafu.io.DataWriter.SaveCSV(path, currentGraph);
                }

                JOptionPane.showMessageDialog(this, "Pomyślnie zapisano współrzędne grafu!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Błąd podczas zapisu pliku:\n" + ex.getMessage(), "Błąd I/O", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Konstrukcja układu w oknie
        add(topPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.WEST); // Przypinamy kontrolki po lewej stronie
    }

    // Odizolowana metoda wykonująca ciężkie obliczenia matematyczne
    private void runCalculations(GraphCanvas canvas) {
        if (lastRunAlgorithm == Algorithm.TUTTE) {
            org.rysowaniegrafu.algorithms.Tutte.runTutteEmbedding(canvas.getGraph(), config);
        } else if (lastRunAlgorithm == Algorithm.FRUCHTERMAN) {
            org.rysowaniegrafu.algorithms.Fruchterman.runFruchterman(canvas.getGraph(), config);
        }
    }
}
