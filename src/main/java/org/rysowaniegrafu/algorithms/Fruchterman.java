package org.rysowaniegrafu.algorithms;

import org.rysowaniegrafu.model.Config;
import org.rysowaniegrafu.model.Edge;
import org.rysowaniegrafu.model.Graph;
import org.rysowaniegrafu.model.Node;
import org.rysowaniegrafu.model.SelectionMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fruchterman {
    public static void runFruchterman(Graph g){
        for (Node node : g.nodes) {
            node.setX(rand.nextDouble() * c.getWidth());
            node.setY(rand.nextDouble() * c.getHeight());
        }
        double k = Math.sqrt((c.getWidth() * c.getHeight()) / n);
      
        for (int i = 0; i < c.getIterations() && temp > 0.00001; i++) {
            temp = loop(nodes, edges, c, k, dx, dy, temp);
        }
    }
    private static void loop(double t, Graph g){
        
    }
}
