package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public abstract class Transaction {
    protected int timestamp;
    protected String description;

    public abstract void print(ObjectMapper objectMapper, ArrayNode arrayNode);
}
