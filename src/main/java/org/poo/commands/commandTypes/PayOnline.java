package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeRate;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class PayOnline extends Command {
    private String cardNumber;
    private double amount;
    private String currency;
    private String description;
    private String commerciant;
    private ArrayList<User> users;
    private ArrayList<ExchangeRate> rates;

    public PayOnline(CommandInput input, ArrayList<User> users, ArrayList<ExchangeRate> rates) {
        this.cardNumber = input.getCardNumber();
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

    public ArrayList<ExchangeRate> getRates() {
        return rates;
    }

    public void setRates(ArrayList<ExchangeRate> rates) {
        this.rates = rates;
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        account.payOnline(card, amount, currency, rates);
                        return;
                    }
                }
            }
        }
    }
}
