package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;


/**
 * The base form of a transaction, which contains only
 * a timestamp and a description
 */
@Setter
@Getter
public class Transaction {
    protected int timestamp;
    protected String description;

    public Transaction() {

    }

    public Transaction(final int timestamp, final String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    /**
     * Adds the transaction to the output in JSON format
     */
    public void print(final ObjectMapper objectMapper, final ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        arrayNode.add(node);
    }
}
