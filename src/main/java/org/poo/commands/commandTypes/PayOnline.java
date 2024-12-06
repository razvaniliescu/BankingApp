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
    private ExchangeGraph rates;

    public PayOnline(CommandInput input, ArrayList<User> users, ExchangeGraph rates) {
        this.cardNumber = input.getCardNumber();
        this.timestamp = input.getTimestamp();
        this.command = input.getCommand();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
        this.description = input.getDescription();
        this.commerciant = input.getCommerciant();
        this.users = users;
        this.rates = rates;
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

    public ExchangeGraph getRates() {
        return rates;
    }

    public void setRates(ExchangeGraph rates) {
        this.rates = rates;
    }

    public void payOnline() {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        double rate = rates.getExchangeRate(this.currency, account.getCurrency());
                        int ok = account.payOnline(card, amount, currency, rate);
                        amount *= rate;
                        if (ok == 0) {
                            user.addTransaction(new CardTransaction(this));
                        } else {
                            user.addTransaction(new InsufficientFunds(this));
                        }
                        return;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Card not found");
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<Transaction> transactions) {
        try {
            payOnline();
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
