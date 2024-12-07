package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commands.commandTypes.SplitPayment;
import java.util.List;

public class SplitPayTransaction extends Transaction {
    private List<String> accounts;
    private String currency;
    private double amount;
    public SplitPayTransaction(SplitPayment command) {
        this.timestamp = command.getTimestamp();
        this.accounts = command.getAccounts();
        this.currency = command.getCurrency();
        this.amount = command.getAmount() / accounts.size();
        this.description = String.format("Split payment of %.2f %s", command.getAmount(), currency);
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void print(ObjectMapper objectMapper, ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
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
