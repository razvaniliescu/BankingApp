package org.poo.accounts;

import org.poo.utils.Utils;

public class Card {
    private String cardNumber;
    private String status;

    public Card() {
        this.cardNumber = Utils.generateCardNumber();
        this.status = "active";
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
