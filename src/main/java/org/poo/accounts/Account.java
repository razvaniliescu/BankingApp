package org.poo.accounts;

import org.poo.commands.commandTypes.SpendingReport;
import org.poo.exchange.ExchangeGraph;
import org.poo.exchange.ExchangeRate;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class Account {
    private String iban;
    private double balance;
    private String currency;
    private String type;
    private ArrayList<Card> cards;
    private double minBalance;
    private ArrayList<Transaction> transactions;

    public Account(String currency, String type) {
        this.iban = Utils.generateIBAN();
        this.balance = 0;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        transactions = new ArrayList<>();
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

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
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

    public int payOnline(Card card, double amount, String currency, double rate) {
        amount *= rate;
        if (this.balance >= amount + minBalance) {
            this.balance -= amount;
            return 0;
        }
        return 1;
    }

    public int sendMoney(Account receiver, double amount, double rate) {
        double received_amount = amount * rate;
        if (this.balance >= amount + minBalance) {
            this.balance -= amount;
            receiver.balance += received_amount;
            return 0;
        }
        return 1;
    }

    public boolean canPay(double amount, String currency, ExchangeGraph rates) {
        amount *= rates.getExchangeRate(this.currency, currency);
        return balance >= amount + minBalance;
    }

    public void pay(double amount, String currency, ExchangeGraph rates) {
        amount *= rates.getExchangeRate(currency, this.currency);
        balance -= amount;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }
}
