package org.poo.commerciants.cashbackStrategies;

import org.poo.commerciants.CashbackStrategy;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.exchange.ExchangeGraph;

public class NoCashback implements CashbackStrategy {
    @Override
    public final void cashback(final Account account, final double amount,
                               final Commerciant commerciant, final ExchangeGraph rates,
                               final String currency) {

    }
}
