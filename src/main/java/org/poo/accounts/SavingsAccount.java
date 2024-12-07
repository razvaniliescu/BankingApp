package org.poo.accounts;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String currency, String type, double interestRate, User user) {
        super(currency, type, user);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void addInterest() {
        balance += interestRate * balance;
    }
}
