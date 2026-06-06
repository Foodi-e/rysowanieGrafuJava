package org.rysowaniegrafu.io;
import org.rysowaniegrafu.model.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GraphReader {
    public static Graph LoadBin(String path_n, String path_e){
        Graph graph = new Graph();
        graph.nodes = loadNodesBinary(path_n)
        graph.edges = loadEdges(path_e,graph)
    }
    public static Graph LoadCSV(String path_n, String path_e){
        Graph graph = new Graph();
        graph.nodes = loadNodesCSV(path_n)
        graph.edges = loadEdges(path_e,graph)
    }
    public static Graph LoadOnlyEdges(String path){
        Graph graph = new Graph();
        graph.edges = loadEdges(path,graph)
    }
    private static List<Node> loadNodesCSV(String path) throws IOException {
        List<Node> nodes = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(path));

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");

            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid CSV node line: " + line);
            }

            int id = Integer.parseInt(parts[0].trim());
            double x = Double.parseDouble(parts[1].trim());
            double y = Double.parseDouble(parts[2].trim());

            nodes.add(new Node(id, x, y, false, 0));
        }

        return nodes;
    }
    private static List<Node> loadNodesBinary(String path) throws IOException {
        List<Node> nodes = new ArrayList<>();

        try (DataInputStream dis =
                     new DataInputStream(
                             new BufferedInputStream(
                                     new FileInputStream(path)))) {

            while (dis.available() > 0) {

                int id = dis.readInt();
                double x = dis.readDouble();
                double y = dis.readDouble();

                nodes.add(new Node(id, x, y, false, 0));
            }
        }

        return nodes;
    }
    private static List<Edge> loadEdges(String path, Graph g) throws IOException {
        List<Edge> edges = new ArrayList<>();

        List<String> lines = Files.readAllLines(Paths.get(path));

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");

            String name = "";
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
                    throw new IllegalArgumentException(
                            "Invalid edge line: " + line);
            }

            edges.add(new Edge(name, from, to, weight));
        }
        return edges;
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

