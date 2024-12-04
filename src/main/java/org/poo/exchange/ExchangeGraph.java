package org.poo.exchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExchangeGraph {
    private Map<String, Map<String, Double>> graph = new HashMap<>();

    public ExchangeGraph(ArrayList<ExchangeRate> ratesList) {
        for (ExchangeRate rate : ratesList) {
            graph.putIfAbsent(rate.getFrom(), new HashMap<>());
            graph.putIfAbsent(rate.getTo(), new HashMap<>());
            graph.get(rate.getFrom()).put(rate.getTo(), rate.getRate());
            graph.get(rate.getTo()).put(rate.getFrom(), 1 / rate.getRate());
        }
        computeAllRates();
    }

    public void computeAllRates() {
        for (String k : graph.keySet()) {
            for (String i : graph.keySet()) {
                for (String j : graph.keySet()) {
                    if (graph.get(i).containsKey(k) && graph.get(k).containsKey(j)) {
                        double newRate = graph.get(i).get(k) * graph.get(k).get(j);
                        graph.get(i).put(j, newRate);
                        graph.get(j).put(i, 1 / newRate);
                    }
                }
            }
        }
    }

    public double getExchangeRate(String from, String to) {
        return graph.get(from).get(to);
    }

    public Map<String, Map<String, Double>> getGraph() {
        return graph;
    }

    public void setGraph(Map<String, Map<String, Double>> graph) {
        this.graph = graph;
    }
}
