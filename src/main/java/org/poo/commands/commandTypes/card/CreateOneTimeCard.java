package org.poo.commands.commandTypes.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.cards.OneTimeCard;
import org.poo.core.User;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation of the createOneTimeCard command
 */
public class CreateOneTimeCard extends CreateCard {
    public CreateOneTimeCard(final CommandInput input) {
        super(input);
    }

    /**
     * Finds the specified account and
     * creates a one-time card associated to it
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode arrayNode,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    if (user.getEmail().equals(email)) {
                        this.card = new OneTimeCard(account, user);
                        account.addCard(card);
                        Transaction t = new Transaction.Builder(timestamp, "New card created")
                                .card(card.getCardNumber())
                                .cardHolder(email)
                                .account(iban)
                                .build();
                        user.addTransaction(t);
                        account.addTransaction(t);
                        return;
                    }
                }
            }
        }
    }
}
