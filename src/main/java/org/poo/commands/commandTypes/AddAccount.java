package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.SavingsAccount;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.AccountTransaction;
import org.poo.transactions.NewAccount;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Objects;

public class AddAccount extends Command {
    private String user;
    private String currency;
    private String type;
    private double interestRate;

    public AddAccount(CommandInput input) {
        super(input);
        this.user = input.getEmail();
        this.currency = input.getCurrency();
        this.type = input.getAccountType();
        this.interestRate = input.getInterestRate();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode, ArrayList<User> users, ExchangeGraph rates) {
        User user = null;
        for (User u : users) {
            if (Objects.equals(u.getEmail(), this.user)) {
                user = u;
                break;
            }
        }
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        Account account = null;
        if (Objects.equals(type, "classic")) {
            account = new Account(currency, type);
        } else if (Objects.equals(type, "savings")) {
            account = new SavingsAccount(currency, type, interestRate);
        }
        user.addAccount(account);
        Transaction t = new NewAccount(timestamp);
        user.addTransaction(t);
        account.addTransaction(t);
    }
}
