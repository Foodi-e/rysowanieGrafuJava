package org.rysowaniegrafu.io;

import org.rysowaniegrafu.model.Edge;
import org.rysowaniegrafu.model.Graph;
import org.rysowaniegrafu.model.Node;

import java.util.Random;

public class MockDataLoader {

    public static Graph createDummyGraph() {
        Graph graph = new Graph();
        Random rand = new Random();

        // 1. Tworzymy wierzchołki.
        // Konstruktor Twojej klasy: Node(int id, double x, double y, boolean isFixed, int degree)
        // Dajemy im losowe współrzędne (od 100 do 500), żeby graf na początku był "rozsypany".
        // Stopień (degree) na razie ustawiamy na 0, bo krawędzie dodamy za chwilę.

        Node n0 = new Node(0, rand.nextDouble() * 400 + 100, rand.nextDouble() * 400 + 100, false, 0);
        Node n1 = new Node(1, rand.nextDouble() * 400 + 100, rand.nextDouble() * 400 + 100, false, 0);
        Node n2 = new Node(2, rand.nextDouble() * 400 + 100, rand.nextDouble() * 400 + 100, false, 0);
        Node n3 = new Node(3, rand.nextDouble() * 400 + 100, rand.nextDouble() * 400 + 100, false, 0);

        // Ten wierzchołek docelowo wyląduje w środku
        Node n4 = new Node(4, rand.nextDouble() * 400 + 100, rand.nextDouble() * 400 + 100, false, 0);

        // Dodajemy je do obiektu Graph
        graph.addNode(n0);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);

        // 2. Tworzymy krawędzie i od razu aktualizujemy "degree" dla wierzchołków
        // Ramka zewnętrzna
        addEdgeHelper(graph, "E0", n0, n1);
        addEdgeHelper(graph, "E1", n1, n2);
        addEdgeHelper(graph, "E2", n2, n3);
        addEdgeHelper(graph, "E3", n3, n0);

        // Połączenia do wierzchołka środkowego
        addEdgeHelper(graph, "E4", n4, n0);
        addEdgeHelper(graph, "E5", n4, n1);
        addEdgeHelper(graph, "E6", n4, n2);
        addEdgeHelper(graph, "E7", n4, n3);

        return graph;
    }

    // Metoda pomocnicza, żeby zachować czystość kodu.
    // Oprócz dodania krawędzi, natychmiast podbija stopień (degree) obu wierzchołków!
    private static void addEdgeHelper(Graph g, String name, Node u, Node v) {
        // Twój konstruktor: Edge(String name, Node u, Node v, double weight)
        g.addEdge(new Edge(name, u, v, 1.0));

        // Aktualizacja stopnia węzłów (niezbędne dla algorytmu Tutte'a)
        u.setDegree(u.getDegree() + 1);
        v.setDegree(v.getDegree() + 1);
    }
}