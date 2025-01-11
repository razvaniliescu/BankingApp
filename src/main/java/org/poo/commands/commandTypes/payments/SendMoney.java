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
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.exceptions.UserNotFoundException;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation for the sendMoney command
 */
@Setter
@Getter
public class SendMoney extends Command {
    private String senderIban;
    private double amount;
    private String receiverIban;
    private String description;
    private String currency;
    private CashbackStrategy cashbackStrategy;

    public SendMoney(final CommandInput input) {
        super(input);
        this.senderIban = input.getAccount();
        this.amount = input.getAmount();
        this.receiverIban = input.getReceiver();
        this.description = input.getDescription();
        this.cashbackStrategy = new NoCashback();
    }

    /**
     * Finds the two accounts and
     * makes a transaction
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        try {
            User sender = null;
            User receiver = null;
            Account senderAccount = null;
            Account receiverAccount = null;
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(senderIban)) {
                        sender = user;
                        senderAccount = account;
                    }
                    if (account.getIban().equals(this.receiverIban)) {
                        receiver = user;
                        receiverAccount = account;
                    }
                }
                if (!senderIban.startsWith("RO")) {
                    return;
                }
                if (user.getAliases().containsKey(senderIban)) {
                    return;
                }
                if (user.getAliases().containsKey(this.receiverIban)) {
                    receiver = user;
                    receiverAccount = user.getAliases().get(this.receiverIban);
                    break;
                }
            }
            if (senderAccount != null && receiverAccount == null) {
                for (Commerciant commerciant : commerciants) {
                    if (commerciant.getAccount().equals(this.receiverIban)) {
                        if (senderAccount.payOnline(amount, rates, senderAccount.getCurrency())) {
                            Transaction tSent = new Transaction.Builder(timestamp, description)
                                    .senderIBAN(senderIban)
                                    .receiverIBAN(receiverIban)
                                    .amount(amount)
                                    .user(sender)
                                    .currency(senderAccount.getCurrency())
                                    .type("sent")
                                    .build();
                            senderAccount.addTransaction(tSent);
                            senderAccount.getUser().addTransaction(tSent);
                            double cashback = 0;
                            if (commerciant.getType().equals("Food") && senderAccount.getCashbackDetails().isFoodCashback()) {
                                cashback += 0.02;
                                senderAccount.getCashbackDetails().setFoodCashback(false);
                            } else if (commerciant.getType().equals("Clothes") && senderAccount.getCashbackDetails().isClothesCashback()) {
                                cashback += 0.05;
                                senderAccount.getCashbackDetails().setClothesCashback(false);
                            } else if (commerciant.getType().equals("Tech") && senderAccount.getCashbackDetails().isTechCashback()) {
                                cashback += 0.1;
                                senderAccount.getCashbackDetails().setTechCashback(false);
                            }
                            if (commerciant.getCashbackStrategy().equals("spendingThreshold")) {
                                senderAccount.getCashbackDetails().spendOnline(amount + senderAccount.getCommission(amount, rates, senderAccount.getCurrency()));
                                setCashbackStrategy(new SpendingTreshhold());
                            } else if (commerciant.getCashbackStrategy().equals("nrOfTransactions")) {
                                if (!senderAccount.getCashbackDetails().getCommerciantTransactions().containsKey(commerciant.getCommerciant())) {
                                    senderAccount.getCashbackDetails().getCommerciantTransactions().put(commerciant.getCommerciant(), 1);
                                } else {
                                    senderAccount.getCashbackDetails().getCommerciantTransactions().merge(commerciant.getCommerciant(), 1, Integer::sum);
                                }
                                setCashbackStrategy(new NrOfTransactions());
                            }
                            cashbackStrategy.cashback(senderAccount, amount, commerciant, rates, currency);
                            senderAccount.addFunds(amount * cashback);
                            senderAccount.checkForUpgrade(amount, rates, senderAccount.getCurrency(), timestamp);
                            return;
                        } else {
                            Transaction tSent = new Transaction.Builder(timestamp, "Insufficient funds").build();
                            sender.addTransaction(tSent);
                            senderAccount.addTransaction(tSent);
                        }
                        return;
                    }
                }
            }
            if (senderAccount == null || receiverAccount == null) {
                throw new UserNotFoundException();
            }
            this.currency = senderAccount.getCurrency();
            double rate = rates.getExchangeRate(senderAccount.getCurrency(),
                    receiverAccount.getCurrency());
            boolean ok = senderAccount.sendMoney(receiverAccount, amount, rate, rates);
            if (ok) {
                Transaction tSent = new Transaction.Builder(timestamp, description)
                        .senderIBAN(senderIban)
                        .receiverIBAN(receiverIban)
                        .amount(amount)
                        .currency(currency)
                        .type("sent")
                        .build();
                amount *= rates.getExchangeRate(currency, receiverAccount.getCurrency());
                currency = receiverAccount.getCurrency();
                Transaction tReceived = new Transaction.Builder(timestamp, description)
                        .senderIBAN(senderIban)
                        .receiverIBAN(receiverIban)
                        .amount(amount)
                        .currency(currency)
                        .type("received")
                        .build();
                sender.addTransaction(tSent);
                senderAccount.addTransaction(tSent);
                receiver.addTransaction(tReceived);
                receiverAccount.addTransaction(tReceived);
                senderAccount.checkForUpgrade(amount, rates, currency, timestamp);
            } else {
                Transaction tSent = new Transaction.Builder(timestamp, "Insufficient funds").build();
                sender.addTransaction(tSent);
                senderAccount.addTransaction(tSent);
            }
        } catch (UserNotFoundException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
