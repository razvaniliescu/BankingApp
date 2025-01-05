package org.poo.commerciants.cashbackStrategies;

import org.poo.commerciants.CashbackStrategy;
import org.poo.core.accounts.Account;

public class NoCashback implements CashbackStrategy {
    @Override
    public void cashback(Account account, double amount) {

    }
}
