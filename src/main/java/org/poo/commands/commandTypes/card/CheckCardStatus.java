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
import org.poo.exceptions.CardNotFoundException;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.exceptions.MyException;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Implementation for the checkCardStatus command
 */
@Setter
@Getter
public class CheckCardStatus extends Command {
    private String cardNumber;

    public CheckCardStatus(final CommandInput input) {
        super(input);
        this.cardNumber = input.getCardNumber();
    }

    /**
     * Checks the specified account's balance and
     * changes its status depending on the minimum balance
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        try {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().equals(cardNumber)) {
                            if (account.getBalance() <= account.getMinBalance()
                                    && !Objects.equals(card.getStatus(), "frozen")) {
                                card.setStatus("frozen");
                                user.addTransaction(new Transaction.Builder(timestamp,
                                        "You have reached the minimum amount of funds, the card will be frozen")
                                        .build());
                                return;
                            }
                            return;
                        }
                    }
                }
            } throw new MyException("Card not found");
        } catch (MyException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
