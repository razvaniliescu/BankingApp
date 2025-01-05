package org.poo.core.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.CashbackDetails;
import org.poo.commerciants.CashbackStrategy;
import org.poo.core.ServicePlans;
import org.poo.core.cards.Card;
import org.poo.core.User;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.exceptions.CardNotFoundException;
import org.poo.exceptions.SavingsAccountException;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class for account elements and operations
 */
@Setter
@Getter
public class Account {
    protected String iban;
    protected double balance;
    protected String currency;
    protected String type;
    protected ArrayList<Card> cards;
    protected ServicePlans.Plans plan;
    protected double minBalance;
    protected ArrayList<Transaction> transactions;
    protected ArrayList<Transaction> onlineTransactions;
    protected User user;
    protected CashbackDetails cashbackDetails;

    public Account(final String currency, final String type, final User user) {
        this.iban = Utils.generateIBAN();
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        transactions = new ArrayList<>();
        onlineTransactions = new ArrayList<>();
        this.user = user;
        if (Objects.equals(user.getOccupation(), "student")) {
            this.plan = ServicePlans.Plans.student;
        } else {
            this.plan = ServicePlans.Plans.standard;
        }
        this.cashbackDetails = new CashbackDetails();
    }

    /**
     * Adds a new card to the account
     * @param card the card to add
     */
    public void addCard(final Card card) {
        this.cards.add(card);
    }

    /**
     * Adds funds to the account
     * @param funds the amount to add
     */
    public void addFunds(final double funds) {
        this.balance += funds;
    }

    /**
     * Removes a card from the account
     * @param card the card to be removed
     */
    public void deleteCard(final Card card) {
        this.cards.remove(card);
    }

    /**
     * Pay online to a commerciant
     * @param amount the amount to pay
     */
    public boolean payOnline(final double amount, final ExchangeGraph rates, String currency) {
        double rate = rates.getExchangeRate(currency, this.currency);
        if (this.balance >= amount * rate + minBalance) {
            this.balance -= (amount + getCommission(amount, rates)) * rate;
            this.cashbackDetails.setAmountSpentOnline(cashbackDetails.getAmountSpentOnline()
                    + (amount + getCommission(amount, rates)) * rate);
            return true;
        }
        return false;
    }

    /**
     * Send money to another account
     * @param receiver the account to send money to
     * @param amount the amount to send
     * @param rate the exchange rate
     */
    public boolean sendMoney(final Account receiver, final double amount, final double rate, final ExchangeGraph rates) {
        double receivedAmount = amount * rate;
        if (this.balance >= amount + minBalance) {
            this.balance -= (amount + getCommission(amount, rates));
            receiver.balance += receivedAmount;
            return true;
        }
        return false;
    }

    /**
     * Checks if an account can pay
     * @param amount the amount to pay
     * @param rate the exchange rate
     */
    public boolean canPay(final double amount, final double rate) {
        return balance >= amount * rate + minBalance;
    }

    /**
     * Adds a new transaction to the account
     * @param t the transaction to add
     */
    public void addTransaction(final Transaction t) {
        transactions.add(t);
    }

    /**
     * Adds a new card transaction to the account
     * @param t the transaction to add
     */
    public void addOnlineTransaction(final Transaction t) {
        onlineTransactions.add(t);
    }

    /**
     * Checks if the account is a savings account
     * @throws SavingsAccountException if it isn't
     */
    public boolean isASavingsAccount() throws SavingsAccountException {
        if (Objects.equals(type, "classic")) {
            throw new SavingsAccountException();
        }
        return true;
    }

    /**
     * Checks if the card exists in the accounts list
     * @param cardNumber the number of the card
     * @throws CardNotFoundException if the card doesn't exist
     */
    public Card checkCard(final String cardNumber) throws CardNotFoundException {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        throw new CardNotFoundException();
    }

    /**
     * Adds a commission to a payment based on the account's plan
     */
    public double getCommission(final double amount, final ExchangeGraph rates) {
         switch (plan) {
             case student, gold: return 0;
             case standard: return amount * 0.002;
             case silver: if (amount * rates.getExchangeRate(currency, "RON") < 500) {
                return 0;
             } else {
                return amount * 0.001;
             }
        }
        return 0;
    }
}
