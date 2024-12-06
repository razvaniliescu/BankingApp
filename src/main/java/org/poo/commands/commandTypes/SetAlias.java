package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Processing;

import java.util.ArrayList;

public class SetAlias extends Command {
    private String email;
    private String alias;
    private String iban;
    private ArrayList<User> users;

    public SetAlias(CommandInput input, ArrayList<User> users) {
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
        this.email = input.getEmail();
        this.alias = input.getAlias();
        this.iban = input.getAccount();
        this.users = users;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public void setAlias() {
        User user = Processing.findUser(email, users);
        Account account = Processing.findAccount(user, email);
        user.addAlias(alias, account);
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<Transaction> transactions) {
        setAlias();
    }
}
