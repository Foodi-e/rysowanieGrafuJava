package org.rysowaniegrafu.io;

import org.rysowaniegrafu.model.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DataReader {
    public static Graph LoadBin(String path_n, String path_e) throws IOException {
        Graph graph = new Graph();
        loadNodesBinary(path_n, graph);
        loadEdges(path_e, graph);
        return graph;
    }

    public static Graph LoadCSV(String path_n, String path_e) throws IOException {
        Graph graph = new Graph();
        loadNodesCSV(path_n, graph);
        loadEdges(path_e, graph);
        return graph;
    }

    public static Graph LoadOnlyEdges(String path) throws IOException {
        Graph graph = new Graph();
        loadEdges(path, graph);
        return graph;
    }

    private static void loadNodesCSV(String path, Graph g) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Wyrażenie regularne "[,\\s]+" dzieli linię po przecinku LUB po białych znakach (spacje, taby)
            String[] parts = line.split("[,\\s]+");

            // Jeśli linia nie ma dokładnie 3 kolumn, ignorujemy ją zamiast rzucać błąd
            if (parts.length != 3) {
                System.out.println("Pominięto linię (nieprawidłowa liczba kolumn): " + line);
                continue;
            }

            try {
                // Próba konwersji tekstu na liczby
                int id = Integer.parseInt(parts[0].trim());
                double x = Double.parseDouble(parts[1].trim());
                double y = Double.parseDouble(parts[2].trim());

                g.addNode(new Node(id, x, y, false, 0));

            } catch (NumberFormatException e) {
                // Jeśli trafimy na nagłówek tekstowy np. "ID X Y", program po prostu go przeskoczy
                System.out.println("Pominięto tekstowy nagłówek: " + line);
            }
        }
    }

    private static void loadNodesBinary(String path, Graph g) throws IOException {
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
            while (dis.available() > 0) {
                int id = dis.readInt();
                double x = dis.readDouble();
                double y = dis.readDouble();
                g.addNode(new Node(id, x, y, false, 0));
            }
        }
    }

    private static void loadEdges(String path, Graph g) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("[,\\s]+");

            String name = "E";
            int from;
            int to;
            double weight = 1.0;

            switch (parts.length) {
                case 2:
                    from = Integer.parseInt(parts[0]);
                    to = Integer.parseInt(parts[1]);
                    break;
                case 3:
                    if (isInteger(parts[0]) && isInteger(parts[1])) {
                        from = Integer.parseInt(parts[0]);
                        to = Integer.parseInt(parts[1]);
                        weight = Double.parseDouble(parts[2]);
                    } else {
                        name = parts[0];
                        from = Integer.parseInt(parts[1]);
                        to = Integer.parseInt(parts[2]);
                    }
                    break;
                case 4:
                    name = parts[0];
                    from = Integer.parseInt(parts[1]);
                    to = Integer.parseInt(parts[2]);
                    weight = Double.parseDouble(parts[3]);
                    break;
                default:
                    System.out.println("Pominięto linię: " + line);
                    continue;
            }

            // Idealne wykorzystanie metody putIfAbsent z Twojego Graph
            g.addNodeAbsent(new Node(from, 0, 0, false, 0));
            g.addNodeAbsent(new Node(to, 0, 0, false, 0));

            // Zabezpieczenie przed brakującymi wierzchołkami
            Node u = g.getNode(from);
            Node v = g.getNode(to);

            if (u == null || v == null) {
                System.err.println("Uwaga: Pominięto krawędź " + name + " - nie znaleziono węzła: " + from + " lub " + to);
                continue; // Przejdź do następnej linii, nie dodawaj tej krawędzi
            }

            g.addEdge(new Edge(name, u, v, weight));

            // Podbijamy degree
            u.setDegree(u.getDegree() + 1);
            v.setDegree(v.getDegree() + 1);
        }
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}