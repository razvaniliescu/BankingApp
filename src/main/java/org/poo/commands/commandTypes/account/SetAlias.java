package org.poo.commands.commandTypes.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.user.User;
import org.poo.commands.Command;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

/**
 * Implementation of the setAlias command
 */
@Setter
@Getter
public class SetAlias extends Command {
    private String email;
    private String alias;
    private String iban;

    public SetAlias(final CommandInput input) {
        super(input);
        this.email = input.getEmail();
        this.alias = input.getAlias();
        this.iban = input.getAccount();
    }

    /**
     * Finds the account specified in the input and
     * adds an entry in the user's alias hash table
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.iban)) {
                    user.addAlias(alias, account);
                    return;
                }
            }
        }
    }
}
