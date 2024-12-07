package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class Report extends Command {
    private int startTimestamp;
    private int endTimestamp;
    private String iban;

    public Report(CommandInput input) {
        super(input);
        this.startTimestamp = input.getStartTimestamp();
        this.endTimestamp = input.getEndTimestamp();
        this.iban = input.getAccount();
    }

    public int getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(int startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public int getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(int endTimestamp) {
        this.endTimestamp = endTimestamp;
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
        node.put("command", command);
        ObjectNode result = objectMapper.createObjectNode();
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    result.put("IBAN", iban);
                    result.put("balance", account.getBalance());
                    result.put("currency", account.getCurrency());
                    ArrayNode transactions = objectMapper.createArrayNode();
                    for (Transaction transaction : account.getTransactions()) {
                        if (transaction.getTimestamp() >= startTimestamp && transaction.getTimestamp() <= endTimestamp) {
                            transaction.print(objectMapper, transactions);
                        }
                    }
                    result.set("transactions", transactions);
                }
            }
        }
        node.set("output", result);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
