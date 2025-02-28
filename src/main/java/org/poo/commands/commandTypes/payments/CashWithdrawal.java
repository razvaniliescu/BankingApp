package org.poo.commands.commandTypes.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.Command;
import org.poo.commerciants.Commerciant;
import org.poo.core.user.User;
import org.poo.core.accounts.Account;
import org.poo.core.cards.Card;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.exceptions.MyException;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation for the cashWithdrawal command
 */
@Getter @Setter
public class CashWithdrawal extends Command {
    private String cardNumber;
    private double amount;
    private String email;
    private String location;

    public CashWithdrawal(final CommandInput input) {
        super(input);
        this.cardNumber = input.getCardNumber();
        this.amount = input.getAmount();
        this.email = input.getEmail();
        this.location = input.getLocation();
    }

    /**
     * Withdraws RON from an ATM using the specified card
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        try {
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    for (Account account : user.getAccounts()) {
                        for (Card card : account.getCards()) {
                            if (card.getCardNumber().equals(cardNumber)) {
                                if (card.getStatus().equals("frozen")) {
                                    Transaction t = new Transaction.Builder(timestamp,
                                            "The card is frozen").build();
                                    user.addTransaction(t);
                                    account.addTransaction(t);
                                    return;
                                }
                                double oldAmount = amount;
                                amount *= rates.getExchangeRate("RON", account.getCurrency());
                                if (account.getBalance() - amount < 0) {
                                    Transaction t = new Transaction.Builder(timestamp,
                                            "Insufficient funds").build();
                                    user.addTransaction(t);
                                    account.addTransaction(t);
                                    return;
                                }
                                if (account.getBalance() - amount < account.getMinBalance()
                                        && account.getMinBalance() > 0) {
                                    Transaction t = new Transaction.Builder(timestamp,
                                            "Cannot perform payment due to "
                                                    + "a minimum balance being set")
                                            .build();
                                    user.addTransaction(t);
                                    account.addTransaction(t);
                                    return;
                                }
                                Transaction t = new Transaction.Builder(timestamp,
                                        "Cash withdrawal of " + oldAmount)
                                        .amount(oldAmount)
                                        .build();
                                user.addTransaction(t);
                                account.addTransaction(t);
                                amount += account.getCommission(amount, rates,
                                        account.getCurrency());
                                account.setBalance(account.getBalance() - amount);
                                return;
                            }
                        }
                    } throw new MyException("Card not found");
                }
            } throw new MyException("User not found");
        } catch (MyException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
