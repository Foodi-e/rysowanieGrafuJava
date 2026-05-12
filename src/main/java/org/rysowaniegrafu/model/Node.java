package org.rysowaniegrafu.model;

public class Node {
    private final int id;
    private double x;
    private double y;
    private boolean is_fixed;
    private int degree;

    public Node(int id, double x, double y, boolean is_fixed, int degree) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.is_fixed = is_fixed;
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

    public boolean isIs_fixed() {
        return is_fixed;
    }

    public void setIs_fixed(boolean is_fixed) {
        this.is_fixed = is_fixed;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}
