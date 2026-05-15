package org.rysowaniegrafu.model;

public class Edge {
    private String name;
    private Node u;
    private Node v;
    private double weight;

    public Edge(String name, Node u, Node v, double weight) {
        this.name = name;
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Node getV() {
        return v;
    }

    public void setV(Node v) {
        this.v = v;
    }

    public Node getU() {
        return u;
    }

    public void setU(Node u) {
        this.u = u;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
