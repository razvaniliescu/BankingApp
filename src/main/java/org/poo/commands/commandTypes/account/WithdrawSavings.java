package org.poo.commands.commandTypes.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.Command;
import org.poo.commerciants.Commerciant;
import org.poo.core.User;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.SavingsAccount;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

@Getter @Setter
public class WithdrawSavings extends Command {
    private String account;
    private double amount;
    private String currency;

    public WithdrawSavings(CommandInput input) {
        super(input);
        this.account = input.getAccount();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
            for (User user : users) {
                for (SavingsAccount savingsAccount : user.getSavingsAccounts()) {
                    if (savingsAccount.getIban().equals(account)) {
                        if (savingsAccount.getUser().getAge() < 21) {
                            Transaction t = new Transaction.Builder(timestamp, "You don't have the minimum age required.").build();
                            savingsAccount.addTransaction(t);
                            user.addTransaction(t);
                            return;
                        }
                        if (user.getAccounts().size() - user.getSavingsAccounts().size() <= 0) {
                            Transaction t = new Transaction.Builder(timestamp, "You do not have a classic account.").build();
                            savingsAccount.addTransaction(t);
                            user.addTransaction(t);
                            return;
                        }
                        double oldAmount = amount;
                        amount *= rates.getExchangeRate(savingsAccount.getCurrency(), currency);
                        for (Account account : user.getAccounts()) {
                            if (account.getCurrency().equals(currency) && account.getType().equals("classic")) {
                                if (savingsAccount.getBalance() - amount < savingsAccount.getMinBalance()) {
                                    Transaction t = new Transaction.Builder(timestamp, "Insufficient funds").build();
                                    user.addTransaction(t);
                                    return;
                                }
                                savingsAccount.setBalance(savingsAccount.getBalance() - amount);
                                account.setBalance(account.getBalance() + oldAmount);
                                Transaction t = new Transaction.Builder(timestamp, "Savings withdrawal")
                                        .amount(oldAmount)
                                        .savingsAccountIban(savingsAccount.getIban())
                                        .classicAccountIban(account.getIban())
                                        .build();
                                account.addTransaction(t);
                                savingsAccount.addTransaction(t);
                                user.addTransaction(t);
                                user.addTransaction(t);
                                System.out.println("Withdrawn " + oldAmount + " " + account.getCurrency() + " from " + savingsAccount.getIban());
                                return;
                            }
                        }
                        Transaction t = new Transaction.Builder(timestamp, "You do not have a classic account.").build();
                        savingsAccount.addTransaction(t);
                        user.addTransaction(t);
                        return;
                    }
                }
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(this.account)) {
                        Transaction t = new Transaction.Builder(timestamp, "Account is not of type savings.").build();
                        user.addTransaction(t);
                        return;
                    }
                }
            }
    }
}
