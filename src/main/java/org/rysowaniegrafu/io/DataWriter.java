package org.rysowaniegrafu.io;

import org.rysowaniegrafu.model.Graph;
import org.rysowaniegrafu.model.Node;

import java.io.*;
import java.util.List;
import java.util.Locale;

public class DataWriter {

    public static void SaveCSV(String path, Graph graph) throws IOException {
        List<Node> nodes = graph.getNodes();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Node node : nodes) {
                String line = String.format(Locale.US, "%d,%.6f,%.6f",
                        node.getId(), node.getX(), node.getY());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static void SaveBin(String path, Graph graph) throws IOException {
        List<Node> nodes = graph.getNodes();

        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(path)))) {
            for (Node node : nodes) {
                dos.writeInt(node.getId());
                dos.writeDouble(node.getX());
                dos.writeDouble(node.getY());
            }
        }
    }
}