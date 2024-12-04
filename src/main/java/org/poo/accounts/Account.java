package org.poo.accounts;

import org.poo.commands.commandTypes.SpendingReport;
import org.poo.exchange.ExchangeRate;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class Account {
    private String iban;
    private double balance;
    private String currency;
    private String type;
    private ArrayList<Card> cards;
    private double minBalance;

    public Account(String currency, String type) {
        this.iban = Utils.generateIBAN();
        this.balance = 0;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(double minBalance) {
        this.minBalance = minBalance;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public void addFunds(double funds) {
        this.balance += funds;
    }

    public void deleteCard(Card card) {
        this.cards.remove(card);
    }

    public void payOnline(Card card, double amount, String currency, ArrayList<ExchangeRate> rates) {
        if (!this.currency.equals(currency)) {
            for (ExchangeRate rate : rates) {
                if (rate.getFrom().equals(this.currency) && rate.getTo().equals(currency)) {
                    amount *= rate.getRate();
                    break;
                }
            }
        }
        if (this.balance >= amount + minBalance) {
            this.balance -= amount;
        }
    }
}
