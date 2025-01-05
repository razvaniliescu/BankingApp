package org.poo.commerciants.cashbackStrategies;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.CashbackStrategy;
import org.poo.core.accounts.Account;

@Getter @Setter
public class NrOfTransactions implements CashbackStrategy {
    @Override
    public void cashback(Account account, double amount) {
        int onlineTransactions = account.getCashbackDetails().getNrOfTransactions();
        switch (onlineTransactions) {
            case 2: account.getCashbackDetails().setFoodCashback(true); return;
            case 5: account.getCashbackDetails().setClothesCashback(true); return;
            case 10: account.getCashbackDetails().setTechCashback(true); return;
            default: return;
        }
    }
}
