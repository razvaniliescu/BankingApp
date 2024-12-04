package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

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

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.iban)) {
                    System.out.println("ok");
                    user.deleteAccount(account);
                    break;
                }
            }
        }
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "deleteAccount");
        ObjectNode result = objectMapper.createObjectNode();
        result.put("success", "Account deleted");
        result.put("timestamp", timestamp );
        node.set("output", result);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
