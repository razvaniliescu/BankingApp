package org.poo.transactions.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.commandTypes.payments.SplitPayment;
import org.poo.transactions.success.SplitPayTransaction;

/**
 * Transaction in case of an error
 * in the splitPay command
 */
@Setter
@Getter
public class SplitPayError extends SplitPayTransaction {
    private String errorMessage;

    public SplitPayError(final SplitPayment command, final String account) {
        super(command);
        this.errorMessage = "Account " + account + " has insufficient funds for a split payment.";
    }

    @Override
    public final void print(final ObjectMapper objectMapper, final ArrayNode arrayNode) {
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
