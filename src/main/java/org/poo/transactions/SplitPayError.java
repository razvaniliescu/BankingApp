package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commands.commandTypes.payments.SplitPayment;

public class SplitPayError extends SplitPayTransaction {
    private String errorMessage;

    public SplitPayError(SplitPayment command, String account) {
        super(command);
        this.errorMessage = "Account " + account + " has insufficient funds for a split payment.";
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void print(ObjectMapper objectMapper, ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        node.put("error", errorMessage);
        node.put("currency", currency);
        node.put("amount", amount);
        ArrayNode accountNode = objectMapper.createArrayNode();
        for (String account : accounts) {
            accountNode.add(account);
        }
        node.set("involvedAccounts", accountNode);
        arrayNode.add(node);
    }
}
