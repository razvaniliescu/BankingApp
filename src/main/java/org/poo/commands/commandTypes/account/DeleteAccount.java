package org.poo.commands.commandTypes.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.exceptions.BalanceNotEmptyException;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation of the deleteAccount command
 */
@Setter
@Getter
public class DeleteAccount extends Command {
    private String iban;
    private String email;

    public DeleteAccount(final CommandInput input) {
        super(input);
        this.iban = input.getAccount();
        this.email = input.getEmail();
    }

    /**
     * Finds the account with the corresponding IBAN
     * and deletes it from the user's account list
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", command);
        ObjectNode result = objectMapper.createObjectNode();
        User accountUser = null;
        try {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(this.iban)) {
                        if (account.getType().equals("business") && !account.getUser().getEmail().equals(email)) {
                            user.addTransaction(new Transaction.Builder(timestamp,
                                    "You are not authorized to make this transaction.")
                                    .build());
                            return;
                        } else {
                            accountUser = user;
                            user.deleteAccount(account);
                            break;
                        }
                    }
                }
            }
            result.put("success", "Account deleted");
            result.put("timestamp", timestamp);
        } catch (BalanceNotEmptyException e) {
            accountUser.addTransaction(new Transaction.Builder(timestamp,
                    "Account couldn't be deleted - there are funds remaining").build());
            result.put("error", e.getMessage());
            result.put("timestamp", timestamp);
        }
        node.set("output", result);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
