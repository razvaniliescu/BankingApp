package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commands.commandTypes.DeleteCard;

public class DelCardTransaction extends Transaction {
    private String iban;
    private String card;
    private String email;
    public DelCardTransaction(DeleteCard command) {
        this.timestamp = command.getTimestamp();
        this.description = "The card has been destroyed";
        this.email = command.getEmail();
        this.iban = command.getIban();
        this.card = command.getCard();
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void print(ObjectMapper objectMapper, ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("account", iban);
        node.put("card", card);
        node.put("cardHolder", email);
        node.put("description", description);
        node.put("timestamp", timestamp);
        arrayNode.add(node);
    }
}
