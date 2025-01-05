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

import java.util.ArrayList;

/**
 * Implementation of the addFunds command
 */
@Setter
@Getter
public class AddFunds extends Command {
    private String iban;
    private double funds;

    public AddFunds(final CommandInput input) {
        super(input);
        this.iban = input.getAccount();
        this.funds = input.getAmount();
    }

    /**
     * Finds the specified account and
     * adds funds to it
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode arrayNode,
                        final ArrayList<User> users, final ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        for (User user: users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.iban)) {
                    account.addFunds(this.funds);
                    return;
                }
            }
        }
    }
}
