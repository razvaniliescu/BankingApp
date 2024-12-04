package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class DeleteCard extends Command {
    private String card;
    private ArrayList<User> users;

    public DeleteCard(CommandInput input, ArrayList<User> users) {
        this.users = users;
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
        this.card = input.getCardNumber();
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(this.card)) {
                        account.deleteCard(card);
                        return;
                    }
                }
            }
        }
    }
}
