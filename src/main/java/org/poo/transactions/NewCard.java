package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commands.commandTypes.CreateCard;

public class NewCard extends Transaction {
    private String card;
    private String cardHolder;
    private String account;

    public NewCard(String card, String cardHolder, String iban, int timestamp) {
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = iban;
        this.timestamp = timestamp;
        this.description = "New card created";
    }

    public NewCard(CreateCard command) {
        this.timestamp = command.getTimestamp();
        this.description = "New card created";
        this.card = command.getCard().getCardNumber();
        this.cardHolder = command.getEmail();
        this.account = command.getIban();
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public void print(ObjectMapper objectMapper, ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        node.put("card", card);
        node.put("cardHolder", cardHolder);
        node.put("account", account);
        arrayNode.add(node);
    }
}
