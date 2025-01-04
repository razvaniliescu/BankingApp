package org.poo.commands.commandTypes.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.core.accounts.Account;
import org.poo.core.cards.Card;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.exceptions.CardNotFoundException;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.error.CardIsFrozenError;
import org.poo.transactions.error.InsufficientFundsError;
import org.poo.transactions.success.CardTransaction;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation for the payOnline command
 */
@Setter
@Getter
public class PayOnline extends Command {
    private String cardNumber;
    private double amount;
    private String currency;
    private String description;
    private String commerciant;
    private String email;

    public PayOnline(final CommandInput input) {
        super(input);
        this.cardNumber = input.getCardNumber();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
        this.description = input.getDescription();
        this.commerciant = input.getCommerciant();
        this.email = input.getEmail();
    }

    /**
     * Finds the specified card and its associated card,
     * checks its balance and makes a transaction
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates) {
        try {
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    Account account = user.checkCard(cardNumber);
                    Card card = account.checkCard(cardNumber);
                    if (card.getStatus().equals("frozen")) {
                        user.addTransaction(new CardIsFrozenError(timestamp));
                        return;
                    }
                    double rate = rates.getExchangeRate(this.currency, account.getCurrency());
                    boolean ok = account.payOnline(amount, rate);
                    amount *= rate;
                    Transaction t;
                    if (ok) {
                        t = new Transaction.Builder(timestamp, "Card payment")
                                .amount(amount)
                                .commerciant(commerciant)
                                .build();
                        account.addOnlineTransaction((CardTransaction) t);
                        user.addTransaction(t);
                        account.addTransaction(t);
                        card.pay(timestamp);
                    } else {
                        t = new Transaction.Builder(timestamp, "Insufficient funds")
                                .build();
                        user.addTransaction(t);
                        account.addTransaction(t);
                    }
                    return;
                }
            }
        } catch (CardNotFoundException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
