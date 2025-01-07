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
    protected String email;

    public Card(final Account account, final String email) {
        this.cardNumber = Utils.generateCardNumber();
        this.status = "active";
        this.account = account;
        this.email = email;
    }

    /**
     * Updates the card when a transaction is made.
     * Does nothing for a regular card
     */
    public void pay(final int timestamp) {

    }
}
