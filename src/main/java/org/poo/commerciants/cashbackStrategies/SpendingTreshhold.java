package org.poo.commerciants.cashbackStrategies;

import org.poo.commerciants.CashbackStrategy;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.utils.Utils;

public class SpendingTreshhold implements CashbackStrategy {
    @Override
    public final void cashback(final Account account, final double amount,
                         final Commerciant commerciant, final ExchangeGraph rates,
                         final String currency) {
        double rate = rates.getExchangeRate(account.getCurrency(), "RON");
        double spending = account.getCashbackDetails().getTotalAmountSpentOnline();
        spending *= rate;
        System.out.println(spending + " " + account.getPlan());
        if (spending >= Utils.SMALL_SPENDING_THRESHOLD
                && spending < Utils.MEDIUM_SPENDING_THRESHOLD) {
            switch (account.getPlan()) {
                case standard, student:
                    account.addFunds(amount * Utils.SMALL_STANDARD_CASHBACK); break;
                case silver:
                    account.addFunds(amount * Utils.MEDIUM_STANDARD_CASHBACK); break;
                case gold:
                    account.addFunds(amount * Utils.LARGE_STANDARD_CASHBACK); break;
                default: break;
            }
        } else if (spending >= Utils.MEDIUM_SPENDING_THRESHOLD
                && spending < Utils.LARGE_SPENDING_THRESHOLD) {
            switch (account.getPlan()) {
                case standard, student:
                    account.addFunds(amount * Utils.SMALL_SILVER_CASHBACK); break;
                case silver:
                    account.addFunds(amount * Utils.MEDIUM_SILVER_CASHBACK); break;
                case gold:
                    account.addFunds(amount * Utils.LARGE_SILVER_CASHBACK); break;
                default: break;
            }
        } else if (spending >= Utils.LARGE_SPENDING_THRESHOLD) {
            switch (account.getPlan()) {
                case standard, student:
                    account.addFunds(amount * Utils.SMALL_GOLD_CASHBACK); break;
                case silver:
                    account.addFunds(amount * Utils.MEDIUM_GOLD_CASHBACK); break;
                case gold:
                    account.addFunds(amount * Utils.LARGE_GOLD_CASHBACK); break;
                default: break;
            }
        }
    }
}
