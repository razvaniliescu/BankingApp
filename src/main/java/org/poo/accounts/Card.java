package org.poo.accounts;

import org.poo.utils.Utils;

public class Card {
    protected String cardNumber;
    protected String status;
    protected Account account;

    public Card(Account account) {
        this.cardNumber = Utils.generateCardNumber();
        this.status = "active";
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void pay(int timestamp) {

    }
}
