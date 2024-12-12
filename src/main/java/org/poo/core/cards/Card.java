package org.poo.core.cards;

import lombok.Getter;
import lombok.Setter;
import org.poo.core.accounts.Account;
import org.poo.utils.Utils;

/**
 * Class for card elements and operations
 */
@Setter
@Getter
public class Card {
    protected String cardNumber;
    protected String status;
    protected Account account;

    public Card(final Account account) {
        this.cardNumber = Utils.generateCardNumber();
        this.status = "active";
        this.account = account;
    }

    /**
     * Updates the card when a transaction is made.
     * Does nothing for a regular card
     */
    public void pay(final int timestamp) {

    }
}
