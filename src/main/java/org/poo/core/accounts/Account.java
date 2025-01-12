package org.poo.core.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.CashbackDetails;
import org.poo.core.cards.Card;
import org.poo.core.user.ServicePlans;
import org.poo.core.user.User;
import org.poo.core.exchange.ExchangeGraph;
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
    protected TreeSet<Transaction> transactions;
    protected TreeSet<Transaction> onlineTransactions;
    protected User user;
    protected CashbackDetails cashbackDetails;
    protected int largeSilverTransactions;
    protected TreeSet<Transaction> deposits;

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
    public boolean payOnline(final double amount, final ExchangeGraph rates,
                             final String transactionCurrency) {
        double rate = rates.getExchangeRate(transactionCurrency, this.currency);
        if (this.balance >= amount * rate + minBalance) {
            double total = (amount + getCommission(amount, rates, transactionCurrency)) * rate;
            this.balance -= total;
            return true;
        }
        return false;
    }

    /**
     * Pay online without commission.
     * Used for the split payment
     * @param amount the amount to pay
     */
    public void payWithoutCommision(final double amount, final ExchangeGraph rates,
                                    final String transactionCurrency) {
        double rate = rates.getExchangeRate(transactionCurrency, this.currency);
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
    public boolean sendMoney(final Account receiver, final double amount,
                             final double rate, final ExchangeGraph rates) {
        double receivedAmount = amount * rate;
        if (this.balance >= amount + getCommission(amount, rates, currency) + minBalance) {
            double total = amount + getCommission(amount, rates, currency);
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
        return !(balance >= amount * rate + minBalance);
    }

    /**
     * Adds a new transaction to the account
     * @param t the transaction to add
     */
    public void addTransaction(final Transaction t) {
        transactions.add(t);
    }

    /**
     * Adds a new deposit to the account
     * @param t the deposit to add
     */
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
     * Adds a commission to a payment based on the account's plan
     */
    public double getCommission(final double amount, final ExchangeGraph rates,
                                final String transactionCurrency) {
         switch (plan) {
             case standard: return amount * Utils.STANDARD_COMMISSION;
             case silver: if (amount * rates.getExchangeRate(transactionCurrency, "RON")
                     < Utils.SILVER_COMMISSION_THRESHOLD) {
                return 0;
             } else {
                return amount * Utils.SILVER_COMMISSION;
             }
             default: return 0;
        }
    }

    /**
     * Checks if the account is eligible for an
     * automatic plan upgrade from silver to gold
     */
    public void checkForUpgrade(final double amount, final ExchangeGraph rates,
                                final String transactionCurrency, final int timestamp) {
        if (amount * rates.getExchangeRate(transactionCurrency, "RON")
                >= Utils.SILVER_TRANSACTION_THRESHOLD
                && plan == ServicePlans.Plans.silver) {
            largeSilverTransactions++;
            if (largeSilverTransactions == Utils.SILVER_TRANSACTIONS) {
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
