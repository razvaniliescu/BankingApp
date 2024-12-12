package org.poo.transactions.success;

import org.poo.transactions.Transaction;

public class FreezeCardTransaction extends Transaction {
    public FreezeCardTransaction(final int timestamp) {
        super(timestamp, "You have reached the minimum amount of funds, the card will be frozen");
    }
}
