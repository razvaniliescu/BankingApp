package org.poo.commands.commandTypes.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.cards.Card;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation for the createCard command
 */
@Setter
@Getter
public class CreateCard extends Command {
    protected String iban;
    protected Card card;
    protected String email;

    public CreateCard(final CommandInput input) {
        super(input);
        this.email = input.getEmail();
        this.iban = input.getAccount();
    }

    /**
     * Finds the specified account and
     * creates a card associated to it
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode arrayNode,
                        final ArrayList<User> users, final ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    if (user.getEmail().equals(email)) {
                        this.card = new Card(account);
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
