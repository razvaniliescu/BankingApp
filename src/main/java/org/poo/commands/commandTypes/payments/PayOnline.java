package org.poo.commands.commandTypes.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.CashbackStrategy;
import org.poo.commerciants.Commerciant;
import org.poo.commerciants.cashbackStrategies.NoCashback;
import org.poo.commerciants.cashbackStrategies.NrOfTransactions;
import org.poo.commerciants.cashbackStrategies.SpendingTreshhold;
import org.poo.core.accounts.Account;
import org.poo.core.cards.Card;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.exceptions.CardNotFoundException;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
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
    private CashbackStrategy cashbackStrategy;

    public PayOnline(final CommandInput input) {
        super(input);
        this.cardNumber = input.getCardNumber();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
        this.description = input.getDescription();
        this.commerciant = input.getCommerciant();
        this.email = input.getEmail();
        this.cashbackStrategy = new NoCashback();
    }

    /**
     * Finds the specified card and its associated card,
     * checks its balance and makes a transaction
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        try {
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    Account account = user.checkCard(cardNumber);
                    Card card = account.checkCard(cardNumber);
                    if (card.getStatus().equals("frozen")) {
                        user.addTransaction(new Transaction.Builder(timestamp, "Card is frozen").build());
                        return;
                    }
                    double rate = rates.getExchangeRate(this.currency, account.getCurrency());
                    boolean ok = account.payOnline(amount, rates, this.currency);
                    amount *= rate;
                    Transaction t;
                    if (ok) {
                        t = new Transaction.Builder(timestamp, "Card payment")
                                .amount(amount)
                                .commerciant(commerciant)
                                .build();
                        account.addOnlineTransaction(t);
                        user.addTransaction(t);
                        account.addTransaction(t);
                        card.pay(timestamp);
                        for (Commerciant commerciant : commerciants) {
                            if (this.commerciant.equals(commerciant.getCommerciant())) {
                                double cashback = 0;
                                if (commerciant.getType().equals("food") && account.getCashbackDetails().isFoodCashback()) {
                                    cashback += 0.02;
                                    account.getCashbackDetails().setFoodCashback(false);
                                } else if (commerciant.getType().equals("clothes") && account.getCashbackDetails().isClothesCashback()) {
                                    cashback += 0.05;
                                    account.getCashbackDetails().setClothesCashback(false);
                                } else if (commerciant.getType().equals("tech") && account.getCashbackDetails().isTechCashback()) {
                                    cashback += 0.1;
                                    account.getCashbackDetails().setTechCashback(false);
                                }
                                if (commerciant.getCashbackStrategy().equals("spendingThreshold")) {
                                    setCashbackStrategy(new SpendingTreshhold());
                                } else if (commerciant.getCashbackStrategy().equals("nrOfTransactions")) {
                                    setCashbackStrategy(new NrOfTransactions());
                                }
                                cashbackStrategy.cashback(account, amount);
                                account.addFunds(amount * cashback);
                                account.getCashbackDetails().transaction();
                            }
                        }
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
