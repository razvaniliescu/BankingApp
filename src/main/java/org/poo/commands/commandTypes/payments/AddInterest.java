package org.poo.commands.commandTypes.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.core.accounts.SavingsAccount;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.exceptions.SavingsAccountException;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;

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
                        final ArrayList<User> users, final ExchangeGraph rates) {
        try {
            for (User user : users) {
                SavingsAccount account = user.checkSavingsAccounts(iban);
                account.addInterest();
                return;
            }
        } catch (SavingsAccountException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
