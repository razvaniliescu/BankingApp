package org.poo.transactions.error;

import org.poo.transactions.Transaction;

public class CardIsFrozenError extends Transaction {
    public CardIsFrozenError(final int timestamp) {
        super(timestamp, "The card is frozen");
    }
}
