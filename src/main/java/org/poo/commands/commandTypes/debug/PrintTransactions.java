package org.poo.commands.commandTypes.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class PrintTransactions extends Command {
    private String user;

    public PrintTransactions(CommandInput input) {
        super(input);
        this.user = input.getEmail();
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", command);
        ArrayNode transactionsArray = objectMapper.createArrayNode();
        for (User user : users) {
            if (user.getEmail().equals(this.user)) {
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
