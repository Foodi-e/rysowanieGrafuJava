package org.rysowaniegrafu.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private final Map<Integer, Node> nodes;
    private final List<Edge> edges;

    public Graph() {
        this.nodes = new HashMap<>();
        this.edges = new ArrayList<>();
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

    public void addNodeAbsent(Node node){
        nodes.putIfAbsent(node.getId(),node);
    }
    
    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public Node getNode(int id) {
        return nodes.get(id);
    }

    public List<Node> getNodes() {
        return new ArrayList<>(nodes.values());
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void clear() {
        nodes.clear();
        edges.clear();
    }
}
