package org.poo.exceptions;

public class AccountNotFoundException extends MyException {
    public AccountNotFoundException() {
        super("Account not found");
    }
}
