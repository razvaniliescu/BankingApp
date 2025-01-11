package org.poo.commerciants;

import org.poo.core.accounts.Account;
import org.poo.core.exchange.ExchangeGraph;

public interface CashbackStrategy {
    /**
     * Apply a cashback strategy
     */
    void cashback(Account account, double amount, Commerciant commerciant,
                  ExchangeGraph rates, String currency);
}
