package org.poo.commands.commandTypes.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.CashbackStrategy;
import org.poo.commerciants.Commerciant;
import org.poo.commerciants.cashbackStrategies.NrOfTransactions;
import org.poo.commerciants.cashbackStrategies.SpendingTreshhold;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.BusinessAccount;
import org.poo.core.cards.Card;
import org.poo.core.user.User;
import org.poo.commands.Command;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.exceptions.MyException;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

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
    }

    /**
     * Finds the specified card and its associated card,
     * checks its balance and makes a transaction
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        if (amount == 0) {
            return;
        }
        try {
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    for (Account account : user.getAccounts()) {
                        for (Card card : account.getCards()) {
                            if (card.getCardNumber().equals(cardNumber)) {
                                if (card.getStatus().equals("frozen")) {
                                    user.addTransaction(new Transaction.Builder(timestamp,
                                            "Card is frozen").build());
                                    return;
                                }
                                double rate = rates.getExchangeRate(currency,
                                        account.getCurrency());
                                if (account.getType().equals("business")
                                        && ((BusinessAccount) account).getEmployees()
                                        .contains(user)) {
                                    if (amount * rate > ((BusinessAccount) account)
                                            .getSpendingLimit()) {
                                        user.addTransaction(new Transaction.Builder(timestamp,
                                                "You are not authorized to "
                                                        + "make this transaction.").build());
                                        return;
                                    }
                                }
                                boolean ok = account.payOnline(amount, rates, currency);
                                amount *= rate;
                                Transaction t;
                                if (ok) {
                                    t = new Transaction.Builder(timestamp, "Card payment")
                                            .amount(amount)
                                            .user(user)
                                            .commerciant(commerciant)
                                            .build();
                                    account.addOnlineTransaction(t);
                                    user.addTransaction(t);
                                    account.addTransaction(t);
                                    card.pay(timestamp);
                                    for (Commerciant com : commerciants) {
                                        if (this.commerciant.equals(com.getCommerciant())) {
                                            double cashback = 0;
                                            if (com.getType().equals("Food")
                                                    && account.getCashbackDetails()
                                                    .isFoodCashback()) {
                                                cashback += Utils.FOOD_CASHBACK;
                                                account.getCashbackDetails()
                                                        .setFoodCashback(false);
                                            } else if (com.getType().equals("Clothes")
                                                    && account.getCashbackDetails()
                                                    .isClothesCashback()) {
                                                cashback += Utils.CLOTHES_CASHBACK;
                                                account.getCashbackDetails()
                                                        .setClothesCashback(false);
                                            } else if (com.getType().equals("Tech")
                                                    && account
                                                    .getCashbackDetails()
                                                    .isTechCashback()) {
                                                cashback += Utils.TECH_CASHBACK;
                                                account.getCashbackDetails()
                                                        .setTechCashback(false);
                                            }
                                            if (com.getCashbackStrategy()
                                                    .equals("spendingThreshold")) {
                                                account.getCashbackDetails().spendOnline(amount
                                                        + account.getCommission(amount,
                                                        rates, account.getCurrency()));
                                                setCashbackStrategy(new SpendingTreshhold());
                                            } else if (com.getCashbackStrategy()
                                                    .equals("nrOfTransactions")) {
                                                if (!account.getCashbackDetails()
                                                        .getCommerciantTransactions()
                                                        .containsKey(this.commerciant)) {
                                                    account.getCashbackDetails()
                                                            .getCommerciantTransactions()
                                                            .put(this.commerciant, 1);
                                                } else {
                                                    account.getCashbackDetails()
                                                            .getCommerciantTransactions()
                                                            .merge(this.commerciant,
                                                                    1, Integer::sum);
                                                }
                                                setCashbackStrategy(new NrOfTransactions());
                                            }
                                            cashbackStrategy.cashback(account, amount,
                                                    com, rates, currency);
                                            account.addFunds(amount * cashback);
                                            account.checkForUpgrade(amount, rates,
                                                    currency, timestamp);
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
                    } throw new MyException("Card not found");
                }
            }
        } catch (MyException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
