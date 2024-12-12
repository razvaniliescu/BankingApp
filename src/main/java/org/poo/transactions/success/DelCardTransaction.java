package org.poo.transactions.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.commandTypes.card.DeleteCard;
import org.poo.transactions.Transaction;

@Setter
@Getter
public class DelCardTransaction extends Transaction {
    private String iban;
    private String card;
    private String email;

    public DelCardTransaction(final String iban, final String card,
                              final String email, final int timestamp) {
        this.timestamp = timestamp;
        this.iban = iban;
        this.card = card;
        this.email = email;
        this.description = "The card has been destroyed";
    }

    public DelCardTransaction(final DeleteCard command) {
        this.timestamp = command.getTimestamp();
        this.description = "The card has been destroyed";
        this.email = command.getEmail();
        this.iban = command.getIban();
        this.card = command.getCardNumber();
    }

    @Override
    public final void print(final ObjectMapper objectMapper, final ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("account", iban);
        node.put("card", card);
        node.put("cardHolder", email);
        node.put("description", description);
        node.put("timestamp", timestamp);
        arrayNode.add(node);
    }
}
