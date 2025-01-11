package org.poo.commerciants;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains information about cashback
 */
@Getter @Setter
public class CashbackDetails {
    private double totalAmountSpentOnline;
    private Map<String, Integer> commerciantTransactions;
    private boolean foodCashback;
    private boolean clothesCashback;
    private boolean techCashback;
    private boolean receivedFoodCashback;
    private boolean receivedClothesCashback;
    private boolean receivedTechCashback;
    private double futureCashback;

    public CashbackDetails() {
        commerciantTransactions = new HashMap<>();
    }

    /**
     * Updates the total amount spent online
     */
    public void spendOnline(final double amount) {
        totalAmountSpentOnline += amount;
    }
}
