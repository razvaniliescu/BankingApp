package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Objects;

public class CheckCardStatus extends Command {
    private String card;

    public CheckCardStatus(CommandInput input) {
        super(input);
        this.card = input.getCardNumber();
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates) {
        try {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().equals(this.card)) {
                            if (account.getBalance() <= account.getMinBalance() && !Objects.equals(card.getStatus(), "frozen")) {
                                card.setStatus("frozen");
                                user.addTransaction(new Transaction(timestamp, "You have reached the minimum amount of funds, the card will be frozen"));
                                return;
                            } else if (account.getBalance() <= account.getMinBalance() + 30) {
                                card.setStatus("warning");
                                return;
                            }
                            return;
                        }
                    }
                }
            }
            throw new IllegalArgumentException("Card not found");
        } catch (IllegalArgumentException e) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", command);
            ObjectNode result = objectMapper.createObjectNode();
            result.put("timestamp", timestamp);
            result.put("description", e.getMessage());
            node.set("output", result);
            node.put("timestamp", timestamp);
            output.add(node);
        }
    }
}
