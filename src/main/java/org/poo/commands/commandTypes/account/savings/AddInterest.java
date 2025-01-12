package org.poo.commands.commandTypes.account.savings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.SavingsAccount;
import org.poo.core.user.User;
import org.poo.commands.Command;
import org.poo.exceptions.MyException;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation for the addInterest command
 */
public class AddInterest extends Command {
    private final String iban;

    public AddInterest(final CommandInput input) {
        super(input);
        this.iban = input.getAccount();
    }

    /**
     * Finds the savings account and adds interest to it
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        try {
            for (User user : users) {
                for (SavingsAccount savingsAccount : user.getSavingsAccounts()) {
                    if (savingsAccount.getIban().equals(iban)) {
                        double interest = savingsAccount.addInterest();
                        Transaction t = new Transaction.Builder(timestamp, "Interest rate income")
                                .currencyFormat(true)
                                .amount(interest)
                                .currency(savingsAccount.getCurrency())
                                .build();
                        savingsAccount.addTransaction(t);
                        user.addTransaction(t);
                        return;
                    }
                }
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        throw new MyException("This is not a savings account");
                    }
                }
            }
        } catch (MyException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
