package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class SetMinBalance extends Command {
    private double amount;
    private String iban;
    private ArrayList<User> users;

    public SetMinBalance(CommandInput input, ArrayList<User> users) {
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
        this.users = users;
        this.amount = input.getAmount();
        this.iban = input.getAccount();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<Transaction> transactions) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    account.setMinBalance(amount);
                    return;
                }
            }
        }
    }
}
