package org.poo.transactions.error;

import org.poo.transactions.Transaction;

public class FundsRemaingError extends Transaction {
    public FundsRemaingError(final int timestamp) {
        super(timestamp, "Account couldn't be deleted - there are funds remaining");
    }
}
