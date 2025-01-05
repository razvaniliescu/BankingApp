package org.poo.commerciants.cashbackStrategies;

import org.poo.commerciants.CashbackStrategy;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.exchange.ExchangeGraph;

public class NoCashback implements CashbackStrategy {
    @Override
    public void cashback(Account account, double amount, Commerciant commerciant, ExchangeGraph rates, String currency) {

    }
}
