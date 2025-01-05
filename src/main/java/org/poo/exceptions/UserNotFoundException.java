package org.poo.exceptions;

public class UserNotFoundException extends MyException {
    public UserNotFoundException() {
        super("User not found");
    }
}
