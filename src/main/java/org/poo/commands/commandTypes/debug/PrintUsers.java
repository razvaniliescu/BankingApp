package org.poo.commands.commandTypes.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class PrintUsers extends Command {
    private final ArrayList<User> users;

    public PrintUsers(CommandInput input, ArrayList<User> users) {
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
        this.users = users;
    }

    private void printUsers(ObjectMapper objectMapper, ArrayNode output) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", command);
        ArrayNode usersArray = objectMapper.createArrayNode();
        for (User user : users) {
            ObjectNode userNode = objectMapper.createObjectNode();
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());
            ArrayNode accountsArray = objectMapper.createArrayNode();
            for (Account account : user.getAccounts()) {
                ObjectNode accountNode = objectMapper.createObjectNode();
                accountNode.put("IBAN", account.getIban());
                accountNode.put("balance", account.getBalance());
                accountNode.put("currency", account.getCurrency());
                accountNode.put("type", account.getType());
                ArrayNode cardsArray = objectMapper.createArrayNode();
                for (Card card: account.getCards()) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    cardNode.put("cardNumber", card.getCardNumber());
                    cardNode.put("status", card.getStatus());
                    cardsArray.add(cardNode);
                }
                accountNode.set("cards", cardsArray);
                accountsArray.add(accountNode);
            }
            userNode.set("accounts", accountsArray);
            usersArray.add(userNode);
        }
        node.set("output", usersArray);
        output.add(node);
    }
    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output) {
        printUsers(objectMapper, output);
    }
}
