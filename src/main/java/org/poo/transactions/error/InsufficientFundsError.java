package org.poo.transactions.error;

import org.poo.transactions.Transaction;

public class InsufficientFundsError extends Transaction {
    public InsufficientFundsError(final int timestamp) {
        super(timestamp, "Insufficient funds");
    }
}
