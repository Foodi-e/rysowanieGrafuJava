package org.rysowaniegrafu.model;

public class Config {
    private String inputPath;
    private String outputPath;
    private boolean useBinary;
    private Algorithm algorithm;

    private double width;
    private double height;
    private double temp;
    private int iterations;

    private double radius;
    private int kFixed;
    private SelectionMode selectionMode;

    public Config() {
        this.inputPath = "";
        this.outputPath = "";
        this.useBinary = false;
        this.algorithm = Algorithm.FR;

        this.width = 800.0;
        this.height = 600.0;
        this.temp = 100.0;
        this.iterations = 200;

        this.radius = 100.0;
        this.kFixed = 3;
        this.selectionMode = SelectionMode.CUSTOM;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public boolean isUseBinary() {
        return useBinary;
    }

    public void setUseBinary(boolean useBinary) {
        this.useBinary = useBinary;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getkFixed() {
        return kFixed;
    }

    public void setkFixed(int kFixed) {
        this.kFixed = kFixed;
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }
}
