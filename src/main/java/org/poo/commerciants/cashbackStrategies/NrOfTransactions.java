package org.poo.commerciants.cashbackStrategies;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.CashbackStrategy;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.utils.Utils;

@Getter @Setter
public class NrOfTransactions implements CashbackStrategy {
    @Override
    public final void cashback(final Account account, final double amount,
                         final Commerciant commerciant, final ExchangeGraph rates,
                         final String currency) {
        int onlineTransactions = account.getCashbackDetails()
                .getCommerciantTransactions()
                .get(commerciant.getCommerciant());
        switch (onlineTransactions) {
            case Utils.FOOD_TRANSACTIONS:
                if (!account.getCashbackDetails().isReceivedFoodCashback()) {
                    account.getCashbackDetails().setFoodCashback(true);
                    account.getCashbackDetails().setReceivedFoodCashback(true);
                }
                return;

            case Utils.CLOTHES_TRANSACTIONS:
                if (!account.getCashbackDetails().isReceivedClothesCashback()) {
                    account.getCashbackDetails().setClothesCashback(true);
                    account.getCashbackDetails().setReceivedClothesCashback(true);
                }
                return;
            case Utils.TECH_TRANSACTIONS:
                if (!account.getCashbackDetails().isReceivedTechCashback()) {
                    account.getCashbackDetails().setTechCashback(true);
                    account.getCashbackDetails().setReceivedTechCashback(true);
                }
                return;
            default:
        }
    }
}
