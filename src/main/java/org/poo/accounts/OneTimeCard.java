package org.poo.accounts;

import org.poo.commands.commandTypes.DeleteCard;
import org.poo.transactions.DelCardTransaction;
import org.poo.transactions.NewCard;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

public class OneTimeCard extends Card {
    public OneTimeCard(Account account) {
        super(account);
    }

    public void pay(int timestamp) {
        Transaction del = new DelCardTransaction(this.account.iban, cardNumber, this.account.user.getEmail(), timestamp);
        account.transactions.add(del);
        account.user.addTransaction(del);
        cardNumber = Utils.generateCardNumber();
        Transaction create = new NewCard(this.cardNumber, this.account.user.getEmail(), this.account.iban, timestamp);
        account.transactions.add(create);
        account.user.addTransaction(create);
    }
}
