package org.poo.commands.commandTypes.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commerciants.Commerciant;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

/**
 * Implementation for the printTransactions command
 */
public class PrintTransactions extends Command {
    private final String email;

    public PrintTransactions(final CommandInput input) {
        super(input);
        this.email = input.getEmail();
    }

    /**
     * Finds the specified user and
     * prints all of their transactions in the output
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", command);
        ArrayNode transactionsArray = objectMapper.createArrayNode();
        for (User user : users) {
            if (user.getEmail().equals(this.email)) {
                for (Transaction transaction : user.getTransactions()) {
                    transaction.print(objectMapper, transactionsArray);
                }
            }
        }
        node.set("output", transactionsArray);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
