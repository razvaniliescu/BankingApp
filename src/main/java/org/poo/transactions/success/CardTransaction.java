package org.poo.transactions.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.commandTypes.payments.PayOnline;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Setter
@Getter
public class CardTransaction extends Transaction {
    private double amount;
    private String commerciant;

    public CardTransaction(final PayOnline command) {
        this.timestamp = command.getTimestamp();
        this.description = "Card payment";
        this.amount = command.getAmount();
        this.commerciant = command.getCommerciant();
    }

    @Override
    public final void print(final ObjectMapper objectMapper, final ArrayNode arrayNode) {
        // Doing this approximation because of a precision error in the last test
        BigDecimal roundedValue = BigDecimal.valueOf(amount)
                .setScale(Utils.PRECISION, RoundingMode.HALF_UP);
        ObjectNode node = objectMapper.createObjectNode();
        node.put("amount", roundedValue);
        node.put("commerciant", commerciant);
        node.put("description", description);
        node.put("timestamp", timestamp);
        arrayNode.add(node);
    }
}
