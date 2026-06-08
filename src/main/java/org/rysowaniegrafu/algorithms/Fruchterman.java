package org.rysowaniegrafu.algorithms;

import org.rysowaniegrafu.model.Config;
import org.rysowaniegrafu.model.Edge;
import org.rysowaniegrafu.model.Graph;
import org.rysowaniegrafu.model.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Fruchterman {
    public static void runFruchterman(Graph g, Config c) {
        Random rand = new Random();
        List<Node> nodes = g.getNodes();
        List<Edge> edges = g.getEdges();
        int nodesSize = nodes.size();
        
        for (Node node : nodes) {
            node.setX(rand.nextDouble() * c.getWidth());
            node.setY(rand.nextDouble() * c.getHeight());
        }
        
        Map<Integer, Integer> idToIndexMap = new HashMap<>();
        for (int i = 0; i < nodesSize; i++) {
            idToIndexMap.put(nodes.get(i).getId(), i);
        }
        
        double[] dx = new double[nodesSize];
        double[] dy = new double[nodesSize];
        
        double k = Math.sqrt((c.getWidth() * c.getHeight()) / nodesSize);
        double temp = c.getTemp();
        for (int i = 0; (i < c.getIterations()) && (temp > 0.00001); i++) {
            temp = loop(nodes, edges, c, k, dx, dy, temp, idToIndexMap);
        }
    }

    private static double loop(List<Node> nodes, List<Edge> edges, Config c, double k, double[] dx, double[] dy, double temp, Map<Integer, Integer> idToIndexMap) {
        int nodesSize = nodes.size();
        for (int i = 0; i < nodesSize; i++) {
            Node u = nodes.get(i);
            for (int j = i + 1; j < nodesSize; j++) {
                Node v = nodes.get(j);
                double fx = u.getX() - v.getX();
                double fy = u.getY() - v.getY();
                double length = Math.sqrt(fx * fx + fy * fy);
                if (length < 0.00001) {
                    continue;
                }
                double repForce = (k * k) / length;
                double forceX = (fx / length) * repForce;
                double forceY = (fy / length) * repForce;
                dx[i] += forceX;
                dy[i] += forceY;
                dx[j] -= forceX;
                dy[j] -= forceY;
            }
        }
        for (Edge edge : edges) {
            Node u = edge.getU();
            Node v = edge.getV();
            double fx = u.getX() - v.getX();
            double fy = u.getY() - v.getY();
            double length = Math.sqrt(fx * fx + fy * fy);
            
            if (length < 0.00001) {
                continue;
            }
            
            double attForce = (edge.getWeight() * length * length) / k;
            double forceX = (fx / length) * attForce;
            double forceY = (fy / length) * attForce;
            
            Integer uIdx = idToIndexMap.get(u.getId());
            Integer vIdx = idToIndexMap.get(v.getId());
            
            if (uIdx != null && vIdx != null) {
                dx[uIdx] -= forceX;
                dy[uIdx] -= forceY;
                vIdx = idToIndexMap.get(v.getId());
                dx[vIdx] += forceX;
                dy[vIdx] += forceY;
            }
        }
        for (int i = 0; i < nodesSize; i++) {
            Node node = nodes.get(i);
            if (node.isFixed()) {
                dx[i] = 0;
                dy[i] = 0;
                continue;
            }
            double length = Math.sqrt(dx[i] * dx[i] + dy[i] * dy[i]);
            if (length < 0.00001) {
                dx[i] = 0;
                dy[i] = 0;
                continue;
            }
            double limitedX = (dx[i] / length) * Math.min(length, temp);
            double limitedY = (dy[i] / length) * Math.min(length, temp);
            node.setX(node.getX() + limitedX);
            node.setY(node.getY() + limitedY);
            
            dx[i] = 0;
            dy[i] = 0;
            
            if (node.getX() < 0.0) {
                node.setX(0.0);
            } else if (node.getX() > c.getWidth()) {
                node.setX(c.getWidth());
            }
            if (node.getY() < 0.0) {
                node.setY(0.0);
            } else if (node.getY() > c.getHeight()) {
                node.setY(c.getHeight());
            }
        }
        return 0.9 * temp;
    }
}
