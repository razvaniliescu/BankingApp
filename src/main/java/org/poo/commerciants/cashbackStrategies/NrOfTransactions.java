package org.poo.commerciants.cashbackStrategies;

import com.sun.source.tree.UsesTree;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.CashbackStrategy;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.exchange.ExchangeGraph;

@Getter @Setter
public class NrOfTransactions implements CashbackStrategy {
    @Override
    public void cashback(Account account, double amount, Commerciant commerciant, ExchangeGraph rates, String currency) {
        int onlineTransactions = account.getCashbackDetails()
                .getCommerciantTransactions()
                .get(commerciant.getCommerciant());
        System.out.println(commerciant.getCommerciant() + " nr of transactions " + onlineTransactions);
        switch (onlineTransactions) {
            case 2: {
                if (!account.getCashbackDetails().isReceivedFoodCashback()) {
                    account.getCashbackDetails().setFoodCashback(true);
                    account.getCashbackDetails().setReceivedFoodCashback(true);
                    System.out.println("Set food cashback to true");
                }
                return;
            }
            case 5: {
                if (!account.getCashbackDetails().isReceivedClothesCashback()) {
                    account.getCashbackDetails().setClothesCashback(true);
                    account.getCashbackDetails().setReceivedClothesCashback(true);
                    System.out.println("Set clothes cashback to true");
                }
                return;
            }
            case 10: {
                if (!account.getCashbackDetails().isReceivedTechCashback()) {
                    account.getCashbackDetails().setTechCashback(true);
                    account.getCashbackDetails().setReceivedTechCashback(true);
                    System.out.println("Set tech cashback to true");
                }
                return;

            }
            default:
        }
    }
}
