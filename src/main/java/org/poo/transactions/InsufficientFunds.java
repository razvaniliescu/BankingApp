package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commands.Command;

public class InsufficientFunds extends Transaction {
    public InsufficientFunds(Command command) {
        this.timestamp= command.getTimestamp();
        this.description = "Insufficient funds";
    }

    @Override
    public void print(ObjectMapper objectMapper, ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("description", description);
        node.put("timestamp", timestamp);
        arrayNode.add(node);
    }
}
