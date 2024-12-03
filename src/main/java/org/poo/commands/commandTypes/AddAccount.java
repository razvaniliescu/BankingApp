package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.Objects;

public class AddAccount extends Command {
    private User user;
    private String currency;
    private String type;

    public AddAccount(CommandInput input, ArrayList<User> users) {
        for (User u : users) {
            if (Objects.equals(u.getEmail(), input.getEmail())) {
                user = u;
            }
        }
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
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

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode) {
        user.addAccount(new Account(currency, type));
    }
}
