package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.Objects;

public class CreateCard extends Command {
    private ArrayList<User> users;
    private String account;

    public CreateCard(CommandInput input, ArrayList<User> users) {
        this.users = users;
        this.account = input.getAccount();
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.account)) {
                    account.addCard(new Card());
                    return;
                }
            }
        }
    }
}
