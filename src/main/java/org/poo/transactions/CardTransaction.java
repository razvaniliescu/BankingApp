package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commands.commandTypes.PayOnline;

public class CardTransaction extends Transaction {
    private double amount;
    private String commerciant;

    public CardTransaction(PayOnline command) {
        this.timestamp = command.getTimestamp();
        this.description = "Card payment";
        this.amount = command.getAmount();
        this.commerciant = command.getCommerciant();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public void setCommerciant(String commerciant) {
        this.commerciant = commerciant;
    }

    @Override
    public void print(ObjectMapper objectMapper, ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("amount", amount);
        node.put("commerciant", commerciant);
        node.put("description", description);
        node.put("timestamp", timestamp);
        arrayNode.add(node);
    }
}
