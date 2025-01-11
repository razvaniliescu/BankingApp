package org.poo.commands.commandTypes.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.User;
import org.poo.commands.Command;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.exceptions.MyException;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

@Setter
@Getter
public class Report extends Command {
    protected int startTimestamp;
    protected int endTimestamp;
    protected String iban;

    public Report(final CommandInput input) {
        super(input);
        this.startTimestamp = input.getStartTimestamp();
        this.endTimestamp = input.getEndTimestamp();
        this.iban = input.getAccount();
    }

    /**
     * Build a report for the specified account
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        try {
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
                            if (transaction.getTimestamp() >= startTimestamp
                                    && transaction.getTimestamp() <= endTimestamp) {
                                transaction.print(objectMapper, transactions);
                            }
                        }
                        result.set("transactions", transactions);
                        node.set("output", result);
                        node.put("timestamp", timestamp);
                        output.add(node);
                        return;
                    }
                }
            }
            throw new MyException("Account not found");
        } catch (MyException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
