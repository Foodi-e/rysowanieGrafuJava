package org.rysowaniegrafu.model;

public class Edge {
    private String name;
    private int u;
    private int v;
    private double weight;

    public Edge(String name, int u, int v, double weight) {
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

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getU() {
        return u;
    }

    public void setU(int u) {
        this.u = u;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
