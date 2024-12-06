package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.AccountTransaction;
import org.poo.transactions.InsufficientFunds;
import org.poo.transactions.Transaction;
import org.poo.utils.Processing;

import java.util.ArrayList;

public class SendMoney extends Command {
    private String iban;
    private double amount;
    private String receiver;
    private String description;
    private ExchangeGraph rates;
    private ArrayList<User> users;
    private String currency;

    public SendMoney(CommandInput input, ArrayList<User> users, ExchangeGraph rates) {
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
        this.iban = input.getAccount();
        this.amount = input.getAmount();
        this.receiver = input.getReceiver();
        this.description = input.getDescription();
        this.rates = rates;
        this.users = users;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExchangeGraph getRates() {
        return rates;
    }

    public void setRates(ExchangeGraph rates) {
        this.rates = rates;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<Transaction> transactions) {
        User sender = null;
        User receiver = null;
        Account senderAccount = null;
        Account receiverAccount = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    sender = user;
                    senderAccount = account;
                    break;
                }
                if (account.getIban().equals(this.receiver)) {
                    receiver = user;
                    receiverAccount = account;
                    break;
                }
            }
            if (user.getAliases().containsKey(iban)) {
                return;
            }
            if (user.getAliases().containsKey(this.receiver)) {
                receiver = user;
                receiverAccount = user.getAliases().get(this.receiver);
                break;
            }
        }
        if (senderAccount == null || receiverAccount == null) {
            return;
        }
        this.currency = senderAccount.getCurrency();
        double rate = rates.getExchangeRate(senderAccount.getCurrency(), receiverAccount.getCurrency());
        int ok = senderAccount.sendMoney(receiverAccount, amount, rate);
        if (ok == 0) {
            sender.addTransaction(new AccountTransaction(this, "sent"));
            receiver.addTransaction(new AccountTransaction(this, "received"));
        } else {
            sender.addTransaction(new InsufficientFunds(this));
        }
    }
}
