package org.poo.commerciants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CashbackDetails {
    private double amountSpentOnline;
    private int nrOfTransactions;
    private boolean foodCashback;
    private boolean clothesCashback;
    private boolean techCashback;

    public void transaction() {
        nrOfTransactions++;
    }
}
