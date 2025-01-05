package org.poo.commands.commandTypes.payments;

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

    public SplitPayment(final CommandInput input) {
        super(input);
        this.accounts = input.getAccounts();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
    }

    /**
     * Finds all the accounts involved in the payment,
     * checks them and makes the transactions
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        Account errorAccount = null;
        int accountsNum = accounts.size();
        Map<Account, User> userAccountMap = new HashMap<>();
        ArrayList<Account> involvedAccounts = new ArrayList<>();
        for (String iban: this.accounts) {
            for (User user: users) {
                for (Account account: user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        involvedAccounts.add(account);
                        userAccountMap.put(account, user);
                        double rate = rates.getExchangeRate(currency, account.getCurrency());
                        if (!account.canPay(amount / accountsNum, rate)) {
                            errorAccount = account;
                        }
                    }
                }
            }
        }
        if (errorAccount == null) {
            for (Account account: involvedAccounts) {
                Transaction t = new Transaction.Builder(timestamp,
                        String.format("Split payment of %.2f %s", amount, currency))
                        .accounts(involvedAccounts)
                        .currency(currency)
                        .amount(amount)
                        .build();
                double rate = rates.getExchangeRate(currency, account.getCurrency());
                account.payOnline(amount / accountsNum, rates, this.currency);
                userAccountMap.get(account).addTransaction(t);
                account.addTransaction(t);
            }
        } else {
            for (Account account: involvedAccounts) {
                Transaction t = new Transaction.Builder(timestamp,
                        String.format("Split payment of %.2f %s", amount, currency))
                        .accounts(involvedAccounts)
                        .currency(currency)
                        .amount(amount)
                        .errorMessage("Account " + errorAccount + " has insufficient funds for a split payment.")
                        .build();
                userAccountMap.get(account).addTransaction(t);
                account.addTransaction(t);
            }
        }
    }
}
