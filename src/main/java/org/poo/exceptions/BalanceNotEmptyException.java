package org.poo.exceptions;

public class BalanceNotEmptyException extends MyException {
    public BalanceNotEmptyException() {
        super("Account couldn't be deleted - see org.poo.transactions for details");
    }
}
