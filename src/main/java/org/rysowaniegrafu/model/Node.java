package org.rysowaniegrafu.model;

public class Node {
    private final int id;
    private double x;
    private double y;
    private boolean isFixed;
    private int degree;

    public Node(int id, double x, double y, boolean isFixed, int degree) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.isFixed = isFixed;
        this.degree = degree;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        this.isFixed = fixed;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}
