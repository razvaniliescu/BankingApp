package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.SavingsAccount;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class AddInterest extends Command {
    private String iban;

    public AddInterest(CommandInput input) {
        super(input);
        this.iban = input.getAccount();
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates) {
        try {
            for (User user : users) {
                for (SavingsAccount account : user.getSavingsAccounts()) {
                    if (account.getIban().equals(iban)) {
                        account.addInterest();
                        return;
                    }
                }
            }
            throw new IllegalArgumentException("This is not a savings account");
        } catch (IllegalArgumentException e) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", command);
            ObjectNode result = objectMapper.createObjectNode();
            result.put("description", e.getMessage());
            result.put("timestamp", timestamp);
            node.set("output", result);
            node.put("timestamp", timestamp);
            output.add(node);
        }
    }
}
