package org.poo.commerciants.cashbackStrategies;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.CashbackStrategy;
import org.poo.core.accounts.Account;

@Getter @Setter
public class NrOfTransactions implements CashbackStrategy {
    private int foodTransactions;
    private int clothesTransactions;
    private int techTransactions;

    @Override
    public void cashback(Account account, double amount) {
        if (foodTransactions == 2) {
            account.addFunds(amount * 0.02);
        } else if (clothesTransactions == 5) {
            account.addFunds(amount * 0.05);
        } else if (techTransactions == 10) {
            account.addFunds(amount * 0.10);
        }
    }
}
