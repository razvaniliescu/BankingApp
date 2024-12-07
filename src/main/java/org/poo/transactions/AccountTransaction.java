package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commands.commandTypes.payments.SendMoney;

public class AccountTransaction extends Transaction {
    private String senderIBAN;
    private String receiverIBAN;
    private double amount;
    private String currency;
    private String type;

    public AccountTransaction(SendMoney command, String type) {
        this.timestamp = command.getTimestamp();
        this.description = command.getDescription();
        this.senderIBAN = command.getIban();
        this.receiverIBAN = command.getReceiver();
        this.amount = command.getAmount();
        this.currency = command.getCurrency();
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public void setSenderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    public String getReceiverIBAN() {
        return receiverIBAN;
    }

    public void setReceiverIBAN(String receiverIBAN) {
        this.receiverIBAN = receiverIBAN;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public void print(ObjectMapper objectMapper, ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        node.put("senderIBAN", senderIBAN);
        node.put("receiverIBAN", receiverIBAN);
        node.put("amount", amount + " " + currency);
        node.put("transferType", type);
        arrayNode.add(node);
    }
}
