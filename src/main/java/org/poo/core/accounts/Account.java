package org.poo.core.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.CashbackDetails;
import org.poo.core.ServicePlans;
import org.poo.core.cards.Card;
import org.poo.core.User;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.exceptions.CardNotFoundException;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.*;

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
    protected List<Card> cards;
    protected ServicePlans.Plans plan;
    protected double minBalance;
    protected Set<Transaction> transactions;
    protected Set<Transaction> onlineTransactions;
    protected User user;
    protected CashbackDetails cashbackDetails;
    protected int largeSilverTransactions;
    protected Set<Transaction> deposits;

    public Account(final String currency, final String type, final User user) {
        this.iban = Utils.generateIBAN();
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        transactions = new TreeSet<>();
        onlineTransactions = new TreeSet<>();
        this.user = user;
        this.plan = user.getBasePlan();
        this.cashbackDetails = new CashbackDetails();
        this.deposits = new TreeSet<>();
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
            double total = (amount + getCommission(amount, rates)) * rate;
            this.balance -= total;
            return true;
        }
        return false;
    }

    public void payWithoutCommision(final double amount, final ExchangeGraph rates, String currency) {
        double rate = rates.getExchangeRate(currency, this.currency);
        if (this.balance >= amount * rate + minBalance) {
            this.balance -= amount * rate;
        }
    }

    /**
     * Send money to another account
     * @param receiver the account to send money to
     * @param amount the amount to send
     * @param rate the exchange rate
     */
    public boolean sendMoney(final Account receiver, final double amount, final double rate, final ExchangeGraph rates) {
        double receivedAmount = amount * rate;
        if (this.balance >= amount + getCommission(amount, rates) + minBalance) {
            double total = amount + getCommission(amount, rates);
            this.balance -= total;
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
    public boolean cannotPay(final double amount, final double rate) {
        double total = amount * rate + minBalance;
        System.out.println(iban + " " + balance + " " + " " + total);
        return !(balance >= amount * rate + minBalance);
    }

    /**
     * Adds a new transaction to the account
     * @param t the transaction to add
     */
    public void addTransaction(final Transaction t) {
        transactions.add(t);
    }

    public void addUserTransaction(final Transaction t) {
        user.addTransaction(t);
    }

    public void addDeposit(final Transaction t) {
        deposits.add(t);
    }

    /**
     * Adds a new card transaction to the account
     * @param t the transaction to add
     */
    public void addOnlineTransaction(final Transaction t) {
        onlineTransactions.add(t);
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

    /**
     * Checks if the account is eligible for an
     * automatic plan upgrade from silver to gold
     */
    public void checkForUpgrade(double amount, final ExchangeGraph rates, String currency, final int timestamp) {
        if (amount * rates.getExchangeRate(currency, "RON") >= 300 && plan == ServicePlans.Plans.silver) {
            largeSilverTransactions++;
            if (largeSilverTransactions == 5) {
                for (Account account : this.getUser().getAccounts()) {
                    account.setPlan(ServicePlans.Plans.gold);
                }
                Transaction t = new Transaction.Builder(timestamp, "Upgrade plan")
                        .newPlanType(ServicePlans.Plans.gold)
                        .iban(this.iban)
                        .build();
                addTransaction(t);
                user.addTransaction(t);
            }
        }
    }
}
