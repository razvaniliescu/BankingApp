package org.poo.commerciants;

import org.poo.core.accounts.Account;
import org.poo.core.exchange.ExchangeGraph;

public interface CashbackStrategy {
    public void cashback(Account account, final double amount, Commerciant commerciant, ExchangeGraph rates, String currency);
}
