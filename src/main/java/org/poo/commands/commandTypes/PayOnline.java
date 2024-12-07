package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.exchange.ExchangeRate;
import org.poo.fileio.CommandInput;
import org.poo.transactions.CardTransaction;
import org.poo.transactions.InsufficientFunds;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class PayOnline extends Command {
    private String cardNumber;
    private double amount;
    private String currency;
    private String description;
    private String commerciant;
    private ArrayList<User> users;

    public PayOnline(CommandInput input) {
        super(input);
        this.cardNumber = input.getCardNumber();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
        this.description = input.getDescription();
        this.commerciant = input.getCommerciant();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public void setCommerciant(String commerciant) {
        this.commerciant = commerciant;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates) {
        try {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().equals(cardNumber)) {
                            if (card.getStatus().equals("frozen")) {
                                user.addTransaction(new Transaction(timestamp, "The card is frozen"));
                                return;
                            }
                            double rate = rates.getExchangeRate(this.currency, account.getCurrency());
                            int ok = account.payOnline(card, amount, currency, rate);
                            amount *= rate;
                            Transaction t;
                            if (ok == 0) {
                                t = new CardTransaction(this);
                            } else {
                                t = new InsufficientFunds(this);
                            }
                            user.addTransaction(t);
                            account.addTransaction(t);
                            return;
                        }
                    }
                }
            }
            throw new IllegalArgumentException("Card not found");
        } catch (IllegalArgumentException e) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", "payOnline");
            ObjectNode result = objectMapper.createObjectNode();
            result.put("timestamp", this.timestamp);
            result.put("description", e.getMessage());
            node.set("output", result);
            node.put("timestamp", this.timestamp);
            output.add(node);
        }
    }
}
