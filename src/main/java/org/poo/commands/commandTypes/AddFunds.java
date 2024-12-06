package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Objects;

public class AddFunds extends Command {
    private String account;
    private ArrayList<User> users;
    private double funds;

    public AddFunds(CommandInput input, ArrayList<User> users) {
        this.users = users;
        this.account = input.getAccount();
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
        this.funds = input.getAmount();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getFunds() {
        return funds;
    }

    public void setFunds(double funds) {
        this.funds = funds;
    }

    public ArrayList<User> getUser() {
        return users;
    }

    public void setUser(ArrayList<User> users) {
        this.users = users;
    }

    private void addFunds() {
        for (User user: users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.account)) {
                    account.addFunds(this.funds);
                    return;
                }
            }
        }
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode, ArrayList<Transaction> transactions) {
        addFunds();
    }
}
