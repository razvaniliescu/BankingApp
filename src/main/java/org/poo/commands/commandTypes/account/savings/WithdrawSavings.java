package org.poo.commands.commandTypes.account.savings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.Command;
import org.poo.commerciants.Commerciant;
import org.poo.core.user.User;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.SavingsAccount;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

/**
 * Implementation for the withdrawSavings command
 */
@Getter @Setter
public class WithdrawSavings extends Command {
    private String account;
    private double amount;
    private String currency;

    public WithdrawSavings(final CommandInput input) {
        super(input);
        this.account = input.getAccount();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
    }

    /**
     * Withdraws money from a savings account
     * and transfers them to a classic account
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
            for (User user : users) {
                for (SavingsAccount savingsAccount : user.getSavingsAccounts()) {
                    if (savingsAccount.getIban().equals(account)) {
                        if (savingsAccount.getUser().getAge() < Utils.MINIMUM_AGE) {
                            Transaction t = new Transaction.Builder(timestamp,
                                    "You don't have the minimum age required.").build();
                            savingsAccount.addTransaction(t);
                            user.addTransaction(t);
                            return;
                        }
                        if (user.getAccounts().size() - user.getSavingsAccounts().size() <= 0) {
                            Transaction t = new Transaction.Builder(timestamp,
                                    "You do not have a classic account.").build();
                            savingsAccount.addTransaction(t);
                            user.addTransaction(t);
                            return;
                        }
                        double oldAmount = amount;
                        amount *= rates.getExchangeRate(savingsAccount.getCurrency(), currency);
                        for (Account acc : user.getAccounts()) {
                            if (acc.getCurrency().equals(currency)
                                    && acc.getType().equals("classic")) {
                                if (savingsAccount.getBalance() - amount
                                        < savingsAccount.getMinBalance()) {
                                    Transaction t = new Transaction.Builder(timestamp,
                                            "Insufficient funds").build();
                                    user.addTransaction(t);
                                    return;
                                }
                                savingsAccount.setBalance(savingsAccount.getBalance() - amount);
                                acc.setBalance(acc.getBalance() + oldAmount);
                                Transaction t = new Transaction.Builder(timestamp,
                                        "Savings withdrawal")
                                        .amount(oldAmount)
                                        .savingsAccountIban(savingsAccount.getIban())
                                        .classicAccountIban(acc.getIban())
                                        .build();
                                acc.addTransaction(t);
                                savingsAccount.addTransaction(t);
                                user.addTransaction(t);
                                user.addTransaction(t);
                                System.out.println("Withdrawn " + oldAmount + " "
                                        + acc.getCurrency() + " from " + savingsAccount.getIban());
                                return;
                            }
                        }
                        Transaction t = new Transaction.Builder(timestamp,
                                "You do not have a classic account.").build();
                        savingsAccount.addTransaction(t);
                        user.addTransaction(t);
                        return;
                    }
                }
                for (Account acc : user.getAccounts()) {
                    if (acc.getIban().equals(this.account)) {
                        Transaction t = new Transaction.Builder(timestamp,
                                "Account is not of type savings.").build();
                        user.addTransaction(t);
                        return;
                    }
                }
            }
    }
}
