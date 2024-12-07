package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.OneTimeCard;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.NewCard;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class CreateOneTimeCard extends CreateCard {
    public CreateOneTimeCard(CommandInput input) {
        super(input);
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode, ArrayList<User> users, ExchangeGraph rates) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    if (user.getEmail().equals(email)) {
                        this.card = new OneTimeCard(account);
                        account.addCard(card);
                        Transaction t = new NewCard(this);
                        user.addTransaction(t);
                        account.addTransaction(t);
                        return;
                    }

                }
            }
        }
    }
}
