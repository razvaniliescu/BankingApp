package org.poo.commerciants.cashbackStrategies;

import org.poo.commerciants.CashbackStrategy;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.exchange.ExchangeGraph;

public class SpendingTreshhold implements CashbackStrategy {
    @Override
    public void cashback(Account account, double amount, Commerciant commerciant, ExchangeGraph rates, String currency) {
        double rate = rates.getExchangeRate(account.getCurrency(), "RON");
        double spending = account.getCashbackDetails().getAmountSpentOnline().get(commerciant.getCommerciant());
        spending *= rate;
        System.out.println(spending + " " + account.getPlan());
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
                case gold: account.addFunds(amount * 0.007); break;
            }
        }
    }
}
