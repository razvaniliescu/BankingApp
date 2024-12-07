package org.poo.commands.commandTypes.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.SavingsAccount;
import org.poo.accounts.User;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.CardTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SpendingsReport extends Report {
    public SpendingsReport(CommandInput input) {
        super(input);
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates) {
        try {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", command);
            ObjectNode result = objectMapper.createObjectNode();
            Map<String, Double> commerciants = new TreeMap<>();
            for (User user : users) {
                for (SavingsAccount account : user.getSavingsAccounts()) {
                    if (account.getIban().equals(iban)) {
                        throw new IllegalCallerException("This kind of report is not supported for a savings account");
                    }
                }
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        result.put("IBAN", iban);
                        result.put("balance", account.getBalance());
                        result.put("currency", account.getCurrency());
                        ArrayNode transactions = objectMapper.createArrayNode();
                        for (CardTransaction transaction: account.getOnlineTransactions()) {
                            if (transaction.getTimestamp() >= startTimestamp && transaction.getTimestamp() <= endTimestamp) {
                                transaction.print(objectMapper, transactions);
                                commerciants.merge(transaction.getCommerciant(), transaction.getAmount(), Double::sum);
                            }
                        }
                        result.set("transactions", transactions);
                        ArrayNode commerciantsArray = objectMapper.createArrayNode();
                        for (Map.Entry<String, Double> entry : commerciants.entrySet()) {
                            ObjectNode commerciantNode = objectMapper.createObjectNode();
                            commerciantNode.put("commerciant", entry.getKey());
                            commerciantNode.put("total", entry.getValue());
                            commerciantsArray.add(commerciantNode);
                        }
                        result.set("commerciants", commerciantsArray);
                        node.set("output", result);
                        node.put("timestamp", timestamp);
                        output.add(node);
                        return;
                    }
                }
            }
            throw new IllegalArgumentException("Account not found");
        } catch (IllegalArgumentException e) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", command);
            ObjectNode result = objectMapper.createObjectNode();
            result.put("description", e.getMessage());
            result.put("timestamp", timestamp);
            node.set("output", result);
            node.put("timestamp", timestamp);
            output.add(node);
        } catch (IllegalCallerException e) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", command);
            ObjectNode result = objectMapper.createObjectNode();
            result.put("error", e.getMessage());
            node.set("output", result);
            node.put("timestamp", timestamp);
            output.add(node);
        }
    }
}
