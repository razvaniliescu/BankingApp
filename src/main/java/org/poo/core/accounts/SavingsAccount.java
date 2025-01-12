package org.poo.core.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.core.user.User;

@Setter
@Getter
public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(final String currency, final String type,
                          final double interestRate, final User user) {
        super(currency, type, user);
        this.interestRate = interestRate;
    }

    /**
     * Adds interest to the account
     */
    public double addInterest() {
        double interest = interestRate * balance;
        balance += interest;
        System.out.println(iban + " Added interest " + interest + " New balance: " + balance);
        return interest;
    }
}
