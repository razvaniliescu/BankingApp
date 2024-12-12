package org.poo.exceptions;

public class CardNotFoundException extends MyException {
    public CardNotFoundException() {
        super("Card not found");
    }
}
