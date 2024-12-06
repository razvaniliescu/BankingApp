package org.poo.commands.commandTypes.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class PrintTransactions extends Command {
    private User user;
    public PrintTransactions(CommandInput input, ArrayList<User> users) {
        for (User u : users) {
            if (u.getEmail().equals(input.getEmail())) {
                user = u;
                break;
            }
        }
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<Transaction> transactions) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", command);
        ArrayNode transactionsArray = objectMapper.createArrayNode();
        for (Transaction transaction : user.getTransactions()) {
            transaction.print(objectMapper, transactionsArray);
        }
        node.set("output", transactionsArray);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
