package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class DeleteAccount extends Command {
    private String iban;

    public DeleteAccount(CommandInput input) {
        super(input);
        this.iban = input.getAccount();
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", "deleteAccount");
        ObjectNode result = objectMapper.createObjectNode();
        User accountUser = null;
        try {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(this.iban)) {
                        accountUser = user;
                        user.deleteAccount(account);
                        break;
                    }
                }
            }
            result.put("success", "Account deleted");
            result.put("timestamp", timestamp );
        } catch (IllegalArgumentException e) {
            accountUser.addTransaction(new Transaction(timestamp, "Account couldn't be deleted - there are funds remaining"));
            result.put("error", e.getMessage());
            result.put("timestamp", timestamp);
        }
        node.set("output", result);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
