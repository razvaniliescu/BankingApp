package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class AddAccount extends Command {
    private User user;
    private String currency;
    private String type;

    public AddAccount(CommandInput input, User user) {
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
        this.user = user;
        this.currency = input.getCurrency();
        this.type = input.getAccountType();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    private void addAccount() {
        user.getAccounts().add(new Account(currency, type));
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode) {
        addAccount();
    }
}
