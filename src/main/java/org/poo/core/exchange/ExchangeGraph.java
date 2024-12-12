package org.poo.core.exchange;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for storing, computing and accessing the exchange rates
 */
@Setter
@Getter
public class ExchangeGraph {
    private Map<String, Map<String, Double>> graph = new HashMap<>();

    public ExchangeGraph(final ArrayList<ExchangeRate> ratesList) {
        for (ExchangeRate rate : ratesList) {
            graph.putIfAbsent(rate.getFrom(), new HashMap<>());
            graph.putIfAbsent(rate.getTo(), new HashMap<>());
            graph.get(rate.getFrom()).put(rate.getTo(), rate.getRate());
            graph.get(rate.getTo()).put(rate.getFrom(), 1 / rate.getRate());
        }
        computeGraph();
    }

    /**
     * Completes the entire graph based on the exchange rates
     * from the input using the Floyd-Warshall algorithm
     */
    public void computeGraph() {
        for (String currency : graph.keySet()) {
            for (String from : graph.keySet()) {
                for (String to : graph.keySet()) {
                    if (graph.get(from).containsKey(currency)
                            && graph.get(currency).containsKey(to)) {
                        double newRate = graph.get(from).get(currency)
                                * graph.get(currency).get(to);
                        graph.get(from).put(to, newRate);
                        graph.get(to).put(from, 1 / newRate);
                    }
                }
            }
        }
    }

    /**
     * Gets the desired exchange rate from the graph
     */
    public double getExchangeRate(final String from, final String to) {
        return graph.get(from).get(to);
    }

}
