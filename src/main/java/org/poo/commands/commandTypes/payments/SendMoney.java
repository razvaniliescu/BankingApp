package org.poo.commands.commandTypes.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.core.accounts.Account;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.error.InsufficientFundsError;
import org.poo.transactions.success.AccountTransaction;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation for the sendMoney command
 */
@Setter
@Getter
public class SendMoney extends Command {
    private String senderIban;
    private double amount;
    private String receiverIban;
    private String description;
    private String currency;

    public SendMoney(final CommandInput input) {
        super(input);
        this.senderIban = input.getAccount();
        this.amount = input.getAmount();
        this.receiverIban = input.getReceiver();
        this.description = input.getDescription();
    }

    /**
     * Finds the two accounts and
     * makes a transaction
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates) {
        User sender = null;
        User receiver = null;
        Account senderAccount = null;
        Account receiverAccount = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(senderIban)) {
                    sender = user;
                    senderAccount = account;
                }
                if (account.getIban().equals(this.receiverIban)) {
                    receiver = user;
                    receiverAccount = account;
                }
            }
            if (user.getAliases().containsKey(senderIban)) {
                return;
            }
            if (user.getAliases().containsKey(this.receiverIban)) {
                receiver = user;
                receiverAccount = user.getAliases().get(this.receiverIban);
                break;
            }
        }
        if (senderAccount == null || receiverAccount == null) {
            return;
        }
        this.currency = senderAccount.getCurrency();
        double rate = rates.getExchangeRate(senderAccount.getCurrency(),
                receiverAccount.getCurrency());
        boolean ok = senderAccount.sendMoney(receiverAccount, amount, rate);
        if (ok) {
            Transaction tSent = new Transaction.Builder(timestamp, description)
                    .senderIBAN(senderIban)
                    .receiverIBAN(receiverIban)
                    .amount(amount)
                    .currency(currency)
                    .type("sent")
                    .build();
            amount *= rates.getExchangeRate(currency, receiverAccount.getCurrency());
            currency = receiverAccount.getCurrency();
            Transaction tReceived = new Transaction.Builder(timestamp, description)
                    .senderIBAN(senderIban)
                    .receiverIBAN(receiverIban)
                    .amount(amount)
                    .currency(currency)
                    .type("received")
                    .build();
            sender.addTransaction(tSent);
            senderAccount.addTransaction(tSent);
            receiver.addTransaction(tReceived);
            receiverAccount.addTransaction(tReceived);
        } else {
            Transaction tSent = new Transaction.Builder(timestamp, "Insufficient funds").build();
            sender.addTransaction(tSent);
            senderAccount.addTransaction(tSent);
        }
    }
}
