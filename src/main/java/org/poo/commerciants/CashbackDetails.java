package org.poo.commerciants;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class CashbackDetails {
    private Map<String, Double> amountSpentOnline;
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
        amountSpentOnline = new HashMap<>();
    }

    public void spendOnline(double amount) {
        totalAmountSpentOnline += amount;
    }
}
