package org.poo.transactions.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.commandTypes.card.CreateCard;
import org.poo.transactions.Transaction;

/**
 * Transaction when creating a new card
 */
@Setter
@Getter
public class NewCard extends Transaction {
    private String card;
    private String cardHolder;
    private String account;

    public NewCard(final String card, final String cardHolder,
                   final String iban, final int timestamp) {
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = iban;
        this.timestamp = timestamp;
        this.description = "New card created";
    }

    public NewCard(final CreateCard command) {
        this.timestamp = command.getTimestamp();
        this.description = "New card created";
        this.card = command.getCard().getCardNumber();
        this.cardHolder = command.getEmail();
        this.account = command.getIban();
    }

    @Override
    public final void print(final ObjectMapper objectMapper, final ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        node.put("card", card);
        node.put("cardHolder", cardHolder);
        node.put("account", account);
        arrayNode.add(node);
    }
}
