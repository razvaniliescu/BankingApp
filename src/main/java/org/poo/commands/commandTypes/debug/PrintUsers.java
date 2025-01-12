package org.poo.commands.commandTypes.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.cards.Card;
import org.poo.core.user.User;
import org.poo.commands.Command;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

/**
 * Implementation for the printUsers command
 */
public class PrintUsers extends Command {
    public PrintUsers(final CommandInput input) {
        super(input);
    }

    /**
     * Prints all the users and their details to the output
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
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
                if (!(account.getType().equals("business") && account.getUser() != user)) {
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
            }
            userNode.set("accounts", accountsArray);
            usersArray.add(userNode);
        }
        node.set("output", usersArray);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
