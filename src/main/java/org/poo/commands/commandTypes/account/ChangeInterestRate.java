package org.poo.commands.commandTypes.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.SavingsAccount;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.exceptions.SavingsAccountException;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation of the changeInterestRate command
 */
@Setter
@Getter
public class ChangeInterestRate extends Command {
    private String iban;
    private double interestRate;

    public ChangeInterestRate(final CommandInput input) {
        super(input);
        this.iban = input.getAccount();
        this.interestRate = input.getInterestRate();
    }

    /**
     * Finds the savings account given in the input
     * and changes its interest rate
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        try {
            for (User user : users) {
                System.out.println(user.getSavingsAccounts().size());
                for (SavingsAccount account : user.getSavingsAccounts()) {
                    System.out.println(account.getIban() + " " + iban);
                    if (account.getIban().equals(iban)) {
                        account.setInterestRate(interestRate);
                        user.addTransaction(new Transaction.Builder(timestamp,
                                "Interest rate of the account changed to " + interestRate)
                                .build());
                        System.out.println("found account");
                        return;
                    }
                }
                for (Account account : user.getAccounts()) {
                    System.out.println(account.getIban() + " " + iban);
                    if (account.getIban().equals(iban)) {
                        System.out.println("found not savings");
                        System.out.println(user.getEmail());
                        throw new SavingsAccountException();
                    }
                }
            }
        } catch (SavingsAccountException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
