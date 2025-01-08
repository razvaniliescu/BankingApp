package org.poo.commands.commandTypes.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.BusinessAccount;
import org.poo.core.cards.Card;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation for the deleteCard command
 */
@Setter
@Getter
public class DeleteCard extends Command {
    private String cardNumber;
    private String email;
    private String iban;

    public DeleteCard(final CommandInput input) {
        super(input);
        this.cardNumber = input.getCardNumber();
        this.email = input.getEmail();
        this.iban = input.getAccount();
    }

    /**
     * Finds the specified card and
     * removes it from account's card list
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode arrayNode,
                        final ArrayList<User> users, final ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.iban)) {
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().equals(this.cardNumber)) {
                            if (account.getType().equals("business")) {
                                if (((BusinessAccount) account).getEmployees().contains(user) && !card.getUser().getEmail().equals(this.email)) {
                                    user.addTransaction(new Transaction.Builder(timestamp,
                                            "You are not authorized to make this transaction.")
                                            .build());
                                    return;
                                }
                            }
                            this.iban = account.getIban();
                            account.deleteCard(card);
                            Transaction t = new Transaction.Builder(timestamp, "The card has been destroyed")
                                    .card(card.getCardNumber())
                                    .account(this.iban)
                                    .cardHolder(this.email)
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
}

