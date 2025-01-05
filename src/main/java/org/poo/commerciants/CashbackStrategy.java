package org.poo.commerciants;

import org.poo.core.accounts.Account;

public interface CashbackStrategy {
    public void cashback(Account account, final double amount);
}
