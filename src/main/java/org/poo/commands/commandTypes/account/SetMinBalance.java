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
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation for the setMinBalance command
 */
@Setter
@Getter
public class SetMinBalance extends Command {
    private double amount;
    private String iban;
    private String email;

    public SetMinBalance(final CommandInput input) {
        super(input);
        this.amount = input.getAmount();
        this.iban = input.getAccount();
        this.email = input.getAccount();
    }

    /**
     * Finds the account with the given IBAN and
     * sets its minimum balance to the specified amount
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    if (account.getType().equals("business") && !account.getUser()
                            .getEmail().equals(email)) {
                        user.addTransaction(new Transaction.Builder(timestamp,
                                "You are not authorized to make this transaction.")
                                .build());
                        return;
                    }
                    account.setMinBalance(amount);
                    return;
                }
            }
        }
    }
}
