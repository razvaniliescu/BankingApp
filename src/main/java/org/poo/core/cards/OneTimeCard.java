package org.poo.core.cards;

import org.poo.core.user.User;
import org.poo.core.accounts.Account;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

/**
 * Subclass for specific one-time card operation
 */
public class OneTimeCard extends Card {
    public OneTimeCard(final Account account, final User user) {
        super(account, user);
    }

    /**
     * Updates a card when a transaction is made.
     * In this case, it generates another card adds
     * the specific transactions to the account
     */
    public void pay(final int timestamp) {
        Transaction del = new Transaction.Builder(timestamp, "The card has been destroyed")
                .card(cardNumber)
                .account(account.getIban())
                .cardHolder(account.getUser().getEmail())
                .build();
        account.addTransaction(del);
        user.addTransaction(del);
        cardNumber = Utils.generateCardNumber();
        Transaction create = new Transaction.Builder(timestamp, "New card created")
                .card(cardNumber)
                .cardHolder(account.getUser().getEmail())
                .account(account.getIban())
                .build();
        account.addTransaction(create);
        user.addTransaction(create);
    }
}
