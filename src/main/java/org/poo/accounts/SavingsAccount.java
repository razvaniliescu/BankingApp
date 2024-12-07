package org.poo.accounts;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String currency, String type, double interestRate) {
        super(currency, type);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
