package org.poo.commands.commandTypes.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.Command;
import org.poo.commerciants.Commerciant;
import org.poo.core.User;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.BusinessAccount;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.exceptions.MyException;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

/**
 * Implementation for the changeSpendingLimit command
 */
@Getter @Setter
public class ChangeSpendingLimit extends Command {
    private String email;
    private String account;
    private double amount;

    public ChangeSpendingLimit(final CommandInput input) {
        super(input);
        email = input.getEmail();
        account = input.getAccount();
        amount = input.getAmount();
    }

    /**
     * Changes the spending limit of the specified business account
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        try {
            for (User user : users) {
                if (user.getEmail().equals(email)) {
                    for (Account acc : user.getAccounts()) {
                        if (acc.getIban().equals(this.account)) {
                            if (acc.getType().equals("business")) {
                                if (user.equals(acc.getUser())) {
                                    ((BusinessAccount) acc).setSpendingLimit(amount);
                                    return;
                                } else {
                                    throw new MyException("You must be owner in "
                                            + "order to change spending limit.");
                                }
                            } else {
                                throw new MyException("This is not a business account");
                            }
                        }
                    }
                }
            }
        } catch (MyException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
