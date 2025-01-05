package org.poo.commerciants.cashbackStrategies;

import org.poo.commerciants.CashbackStrategy;
import org.poo.core.accounts.Account;

public class SpendingTreshhold implements CashbackStrategy {
    @Override
    public void cashback(Account account, double amount) {
        double spending = account.getCashbackDetails().getAmountSpentOnline();
        if (spending >= 100 && spending < 300) {
            switch (account.getPlan()) {
                case standard, student: account.addFunds(amount * 0.001); break;
                case silver: account.addFunds(amount * 0.003); break;
                case gold: account.addFunds(amount * 0.005); break;
            }
        } else if (spending >= 300 && spending < 500) {
            switch (account.getPlan()) {
                case standard, student: account.addFunds(amount * 0.002); break;
                case silver: account.addFunds(amount * 0.004); break;
                case gold: account.addFunds(amount * 0.0055); break;
            }
        } else if (spending >= 500) {
            switch (account.getPlan()) {
                case standard, student: account.addFunds(amount * 0.0025); break;
                case silver: account.addFunds(amount * 0.005); break;
                case gold: account.addFunds(amount * 0.0075); break;
            }
        }
    }
}
