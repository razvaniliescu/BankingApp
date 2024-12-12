package org.poo.transactions.success;

import org.poo.transactions.Transaction;

public class ChangeInterestTransaction extends Transaction {
    public ChangeInterestTransaction(final int timestamp, final double interestRate) {
        super(timestamp, "Interest rate of the account changed to " + interestRate);
    }
}
