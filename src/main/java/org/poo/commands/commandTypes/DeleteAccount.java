package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class DeleteAccount extends Command {
    private String iban;
    private ArrayList<User> users;

    public DeleteAccount(CommandInput input, ArrayList<User> users) {
        this.users = users;
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
        this.iban = input.getAccount();
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

    public void deleteAccount() {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.iban)) {
                    user.deleteAccount(account);
                    break;
                }
            }
        }
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<Transaction> transactions) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "deleteAccount");
        ObjectNode result = objectMapper.createObjectNode();
        try {
            deleteAccount();
            result.put("success", "Account deleted");
            result.put("timestamp", timestamp );
        } catch (IllegalArgumentException e) {
            result.put("error", e.getMessage());
            result.put("timestamp", timestamp);
        }
        node.set("output", result);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
