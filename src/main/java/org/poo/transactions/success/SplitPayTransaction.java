package org.poo.transactions.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.commandTypes.payments.SplitPayment;
import org.poo.transactions.Transaction;

import java.util.List;

/**
 * Transaction for a split pay
 */
@Setter
@Getter
public class SplitPayTransaction extends Transaction {
    protected List<String> accounts;
    protected String currency;
    protected double amount;
    public SplitPayTransaction(final SplitPayment command) {
        this.timestamp = command.getTimestamp();
        this.accounts = command.getAccounts();
        this.currency = command.getCurrency();
        this.amount = command.getAmount() / accounts.size();
        this.description = String.format("Split payment of %.2f %s", command.getAmount(), currency);
    }

    /**
     * Adds the split pay transaction to the output in JSON format
     */
    @Override
    public void print(final ObjectMapper objectMapper, final ArrayNode arrayNode) {
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
