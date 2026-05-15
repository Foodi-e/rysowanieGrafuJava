package org.rysowaniegrafu.algorithms;

import org.rysowaniegrafu.model.Config;
import org.rysowaniegrafu.model.Edge;
import org.rysowaniegrafu.model.Graph;
import org.rysowaniegrafu.model.Node;
import org.rysowaniegrafu.model.SelectionMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tutte {

    public static void runTutteEmbedding(Graph graph, Config config) {
        List<Node> nodes = graph.getNodes();
        List<Edge> edges = graph.getEdges();
        int numNodes = nodes.size();

        if (numNodes < 3) {
            System.out.println("Błąd: graf musi mieć conajmniej 3 wierzchołki");
            return;
        }
        if (config.getKFixed() < 3) {
            System.out.println("Błąd: Tutte wymaga conajmniej 3 stałe wierzchołki");
            return;
        }

        for (Node node : nodes) {
            node.setFixed(false);
        }

        // wyznaczanie ramki
        setupBoundary(nodes, config);

        if (!isGraphConnectedToBoundary(nodes, edges)) {
            System.out.println("Błąd: Graf jest niespójny! Przerwano algorytm Tuttego.");
            return;
        }

        // mapowanie obiektów Node na ich pozycję w macierzy
        Map<Node, Integer> nodeToMatrixRow = new HashMap<>();
        int numInternal = 0;

        for (Node node : nodes) {
            if (!node.isFixed()) {
                nodeToMatrixRow.put(node, numInternal);
                numInternal++;
            }
        }

        if (numInternal == 0) return;

        double[] M_x = new double[numInternal * numInternal];
        double[] M_y = new double[numInternal * numInternal];
        double[] b_x = new double[numInternal];
        double[] b_y = new double[numInternal];

        // zapełniamy macierz
        for (Edge edge : edges) {
            Node u = edge.getU();
            Node v = edge.getV();
            double w = edge.getWeight();

            Integer rowU = nodeToMatrixRow.get(u); // null jeśli wierzchołek jest stały
            Integer rowV = nodeToMatrixRow.get(v);

            // Wiersz U
            if (rowU != null) {
                int u_u = rowU * numInternal + rowU;
                M_x[u_u] += w;
                M_y[u_u] += w;

                if (rowV != null) {
                    int u_v = rowU * numInternal + rowV;
                    M_x[u_v] -= w;
                    M_y[u_v] -= w;
                } else {
                    b_x[rowU] += w * v.getX();
                    b_y[rowU] += w * v.getY();
                }
            }

            // Wiersz V
            if (rowV != null) {
                int v_v = rowV * numInternal + rowV;
                M_x[v_v] += w;
                M_y[v_v] += w;

                if (rowU != null) {
                    int v_u = rowV * numInternal + rowU;
                    M_x[v_u] -= w;
                    M_y[v_u] -= w;
                } else {
                    b_x[rowV] += w * u.getX();
                    b_y[rowV] += w * u.getY();
                }
            }
        }

        // Rozwiązanie układu równań
        solveLinearSystem(M_x, b_x, numInternal);
        solveLinearSystem(M_y, b_y, numInternal);

        // Przepisanie wyników do wierzchołków
        int matrixRow = 0;
        for (Node node : nodes) {
            if (!node.isFixed()) {
                node.setX(b_x[matrixRow]);
                node.setY(b_y[matrixRow]);
                matrixRow++;
            }
        }
    }

    private static void setupBoundary(List<Node> nodes, Config config) {
        int k = Math.min(config.getKFixed(), nodes.size());
        Node[] boundaryNodes = new Node[k];

        if (config.getSelectionMode() == SelectionMode.CUSTOM) {
            for (int i = 0; i < k; i++) {
                boundaryNodes[i] = nodes.get(i);
                nodes.get(i).setFixed(true);
            }
        } else { // HIGH_DEGREE
            for (int i = 0; i < k; i++) {
                Node bestNode = findHighestDegreeUnfixed(nodes);
                if (bestNode != null) {
                    boundaryNodes[i] = bestNode;
                    bestNode.setFixed(true);
                } else {
                    k = i;
                    break;
                }
            }
        }

        // rozmieszczenie na okręgu
        for (int i = 0; i < k; i++) {
            Node node = boundaryNodes[i];
            double angle = 2.0 * Math.PI * i / k;
            node.setX(config.getRadius() * Math.cos(angle));
            node.setY(config.getRadius() * Math.sin(angle));
        }
    }

    private static Node findHighestDegreeUnfixed(List<Node> nodes) {
        int maxDeg = -1;
        Node bestNode = null;

        for (Node node : nodes) {
            if (!node.isFixed() && node.getDegree() > maxDeg) {
                maxDeg = node.getDegree();
                bestNode = node;
            }
        }
        return bestNode;
    }

    private static void solveLinearSystem(double[] M, double[] b, int n) {
        for (int k = 0; k < n; k++) {
            int pivotIdx = k * n + k;
            double pivot = M[pivotIdx];

            for (int j = k; j < n; j++) {
                M[k * n + j] /= pivot;
            }
            b[k] /= pivot;

            for (int i = k + 1; i < n; i++) {
                double factor = M[i * n + k];
                for (int j = k; j < n; j++) {
                    M[i * n + j] -= factor * M[k * n + j];
                }
                b[i] -= factor * b[k];
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                b[i] -= M[i * n + j] * b[j];
            }
        }
    }

    private static boolean isGraphConnectedToBoundary(List<Node> nodes, List<Edge> edges) {
        Map<Node, Boolean> visited = new HashMap<>();
        int visitedCount = 0;

        for (Node node : nodes) {
            if (node.isFixed()) {
                visited.put(node, true);
                visitedCount++;
            } else {
                visited.put(node, false);
            }
        }

        if (visitedCount == 0) return false;

        boolean changed;
        do {
            changed = false;
            for (Edge edge : edges) {
                Node u = edge.getU();
                Node v = edge.getV();

                if (visited.get(u) && !visited.get(v)) {
                    visited.put(v, true);
                    visitedCount++;
                    changed = true;
                } else if (visited.get(v) && !visited.get(u)) {
                    visited.put(u, true);
                    visitedCount++;
                    changed = true;
                }
            }
        } while (changed);

        return visitedCount == nodes.size();
    }
}
