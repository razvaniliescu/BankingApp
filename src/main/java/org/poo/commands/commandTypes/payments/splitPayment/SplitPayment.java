package org.poo.commands.commandTypes.payments.splitPayment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation for the splitPayment command
 */
@Setter
@Getter
public class SplitPayment extends Command {
    private List<String> accounts;
    private double amount;
    private String currency;
    private String type;
    private List<Double> amountForUsers;
    private List<User> usersInvolved;
    private int usersToAccept;

    public SplitPayment(final CommandInput input) {
        super(input);
        this.accounts = input.getAccounts();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
        this.type = input.getSplitPaymentType();
        this.amountForUsers = input.getAmountForUsers();
        this.usersInvolved = new ArrayList<>();
        usersToAccept = accounts.size();
    }

    /**
     * Adds the split payment to all the accounts
     * @param objectMapper used to create JSON objects
     * @param output       the array node that will be written in the output file
     * @param users        the list of all users
     * @param rates        the exchange rate graph
     * @param commerciants the list of commericants
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        for (String iban : accounts) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (iban.equals(account.getIban())) {
                        user.getPendingPayments().add(this);
                    }
                }
            }
        }
    }

    /**
     * Finds all the accounts involved in this payment
     * and removes this from their pending list
     */
    public void rejectPayment(final ArrayList<User> users) {
        for (String iban : accounts) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (iban.equals(account.getIban())) {
                        user.getPendingPayments().remove(this);
                        Transaction t = new Transaction.Builder(timestamp,
                                String.format("Split payment of %.2f %s", amount, currency))
                                .amountForUsers(amountForUsers)
                                .accountsInvolved(accounts)
                                .splitPaymentType(type)
                                .splitPaymentCurrency(currency)
                                .errorMessage("One user rejected the payment.")
                                .build();
                        user.addTransaction(t);
                        account.addTransaction(t);
                    }
                }
            }
        }
    }


    /**
     * Finds all the accounts involved in the payment,
     * checks them and makes the transactions
     */
    public void processPayment(final ArrayList<User> users, final ExchangeGraph rates) {
        Account errorAccount = null;
        int accountsNum = accounts.size();
        Map<Account, User> userAccountMap = new HashMap<>();
        ArrayList<Account> involvedAccounts = new ArrayList<>();
        if (type.equals("custom")) {
            int index = 0;
            for (String iban: accounts) {
                for (User user: users) {
                    for (Account account : user.getAccounts()) {
                        if (iban.equals(account.getIban())) {
                            involvedAccounts.add(account);
                            userAccountMap.put(account, user);
                            double rate = rates.getExchangeRate(currency, account.getCurrency());
                            if (account.cannotPay(amountForUsers.get(index), rate)) {
                                if (errorAccount == null) {
                                    errorAccount = account;
                                }
                            }
                            index++;
                        }
                    }
                }
            }
            index = 0;
            if (errorAccount == null) {
                Transaction t = new Transaction.Builder(timestamp,
                        String.format("Split payment of %.2f %s", amount, currency))
                        .amountForUsers(amountForUsers)
                        .splitPaymentType(type)
                        .accounts(involvedAccounts)
                        .currency(currency)
                        .splitPaymentCurrency(currency)
                        .build();
                for (Account account : involvedAccounts) {
                    account.payWithoutCommision(amountForUsers.get(index), rates, this.currency);
                    account.addTransaction(t);
                    index++;
                }
                for (User user : usersInvolved) {
                    user.addTransaction(t);
                }
            } else {
                Transaction t = new Transaction.Builder(timestamp,
                        String.format("Split payment of %.2f %s", amount, currency))
                        .amountForUsers(amountForUsers)
                        .splitPaymentType(type)
                        .splitPaymentCurrency(currency)
                        .accounts(involvedAccounts)
                        .errorMessage("Account " + errorAccount.getIban()
                                + " has insufficient funds for a split payment.")
                        .build();
                for (Account account: involvedAccounts) {
                    account.addTransaction(t);
                }
                for (User user : usersInvolved) {
                    user.addTransaction(t);
                }
            }
        } else {
            for (String iban: accounts) {
                for (User user: users) {
                    for (Account account: user.getAccounts()) {
                        if (account.getIban().equals(iban)) {
                            involvedAccounts.add(account);
                            userAccountMap.put(account, user);
                            double rate = rates.getExchangeRate(currency, account.getCurrency());
                            if (account.cannotPay(amount / accountsNum, rate)) {
                                if (errorAccount == null) {
                                    errorAccount = account;
                                }
                            }
                        }
                    }
                }
            }
            if (errorAccount == null) {
                Transaction t = new Transaction.Builder(timestamp,
                        String.format("Split payment of %.2f %s", amount, currency))
                        .accounts(involvedAccounts)
                        .splitPaymentType("equal")
                        .splitPaymentCurrency(currency)
                        .amount(amount / accountsNum)
                        .build();
                for (Account account: involvedAccounts) {
                    account.payWithoutCommision(amount / accountsNum, rates, this.currency);
                    account.addTransaction(t);
                }
                for (User user : usersInvolved) {
                    user.addTransaction(t);
                }
            } else {
                Transaction t = new Transaction.Builder(timestamp,
                        String.format("Split payment of %.2f %s", amount, currency))
                        .accounts(involvedAccounts)
                        .splitPaymentCurrency(currency)
                        .splitPaymentType("equal")
                        .amount(amount / accountsNum)
                        .currencyFormat(true)
                        .errorMessage("Account " + errorAccount.getIban()
                                + " has insufficient funds for a split payment.")
                        .build();
                for (Account account: involvedAccounts) {
                    account.addTransaction(t);
                }
                for (User user : usersInvolved) {
                    user.addTransaction(t);
                }
            }
        }

    }
}
