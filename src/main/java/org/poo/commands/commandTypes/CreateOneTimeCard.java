package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.OneTimeCard;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class CreateOneTimeCard extends CreateCard {
    public CreateOneTimeCard(CommandInput input, ArrayList<User> users) {
        super(input, users);
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.account)) {
                    account.addCard(new OneTimeCard());
                    return;
                }
            }
        }
    }
}
