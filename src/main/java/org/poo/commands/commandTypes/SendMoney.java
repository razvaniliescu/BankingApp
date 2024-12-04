package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class SendMoney extends Command {
    private String iban;
    private double amount;
    private String receiver;
    private String description;
    private ExchangeGraph rates;
    private ArrayList<User> users;

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

    public void sendMoney() {
        Account sender = null;
        Account receiver = null;
        for (User user: users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    sender = account;
                }
                if (account.getIban().equals(this.receiver)) {
                    receiver = account;
                }
            }
        }
        if (sender == null || receiver == null) {
            return;
        }
        double rate = rates.getExchangeRate(sender.getCurrency(), receiver.getCurrency());
        sender.sendMoney(receiver, amount, rate);
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output) {
        sendMoney();
    }
}
