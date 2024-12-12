package org.poo.commands.commandTypes.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.SavingsAccount;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.exceptions.SavingsAccountException;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.success.ChangeInterestTransaction;

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
                        final ArrayList<User> users, final ExchangeGraph rates) {
        try {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        if (account.isASavingsAccount()) {
                            ((SavingsAccount) account).setInterestRate(interestRate);
                            user.addTransaction(new
                                    ChangeInterestTransaction(timestamp, interestRate));
                        }
                    }
                }
            }
        } catch (SavingsAccountException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
