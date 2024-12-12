package org.poo.core.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.core.cards.Card;
import org.poo.core.User;
import org.poo.exceptions.CardNotFoundException;
import org.poo.exceptions.SavingsAccountException;
import org.poo.transactions.success.CardTransaction;
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
    protected double minBalance;
    protected ArrayList<Transaction> transactions;
    protected ArrayList<CardTransaction> onlineTransactions;
    protected User user;

    public Account(final String currency, final String type, final User user) {
        this.iban = Utils.generateIBAN();
        this.balance = 0;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        transactions = new ArrayList<>();
        onlineTransactions = new ArrayList<>();
        this.user = user;
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
     * @param rate the exchange rate
     */
    public boolean payOnline(final double amount, final double rate) {
        if (this.balance >= amount * rate + minBalance) {
            this.balance -= amount * rate;
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
    public boolean sendMoney(final Account receiver, final double amount, final double rate) {
        double receivedAmount = amount * rate;
        if (this.balance >= amount + minBalance) {
            this.balance -= amount;
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
    public void addOnlineTransaction(final CardTransaction t) {
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
}
