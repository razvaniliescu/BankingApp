package org.poo.transactions.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.commandTypes.payments.SendMoney;
import org.poo.transactions.Transaction;

@Setter
@Getter
public class AccountTransaction extends Transaction {
    private String senderIBAN;
    private String receiverIBAN;
    private double amount;
    private String currency;
    private String type;

    public AccountTransaction(final SendMoney command, final String type) {
        this.timestamp = command.getTimestamp();
        this.description = command.getDescription();
        this.senderIBAN = command.getSenderIban();
        this.receiverIBAN = command.getReceiverIban();
        this.amount = command.getAmount();
        this.currency = command.getCurrency();
        this.type = type;
    }

    @Override
    public final void print(final ObjectMapper objectMapper, final ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        node.put("senderIBAN", senderIBAN);
        node.put("receiverIBAN", receiverIBAN);
        node.put("amount",  amount + " " + currency);
        node.put("transferType", type);
        arrayNode.add(node);
    }
}
