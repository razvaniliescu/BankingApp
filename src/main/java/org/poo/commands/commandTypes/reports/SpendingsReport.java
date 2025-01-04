package org.poo.commands.commandTypes.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.SavingsAccount;
import org.poo.core.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.exceptions.AccountTypeException;
import org.poo.exceptions.MyException;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SpendingsReport extends Report {
    public SpendingsReport(final CommandInput input) {
        super(input);
    }

    /**
     * Build a spendings report for the specified account
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates) {
        try {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", command);
            ObjectNode result = objectMapper.createObjectNode();
            Map<String, Double> commerciants = new TreeMap<>();
            for (User user : users) {
                for (SavingsAccount account : user.getSavingsAccounts()) {
                    if (account.getIban().equals(iban)) {
                        throw new AccountTypeException();
                    }
                }
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        result.put("IBAN", iban);
                        result.put("balance", account.getBalance());
                        result.put("currency", account.getCurrency());
                        ArrayNode transactions = objectMapper.createArrayNode();
                        for (Transaction transaction: account.getOnlineTransactions()) {
                            if (transaction.getTimestamp() >= startTimestamp
                                    && transaction.getTimestamp() <= endTimestamp) {
                                transaction.print(objectMapper, transactions);
                                commerciants.merge(transaction.getCommerciant(),
                                        transaction.getAmount(), Double::sum);
                            }
                        }
                        result.set("transactions", transactions);
                        ArrayNode commerciantArray = objectMapper.createArrayNode();
                        for (Map.Entry<String, Double> entry : commerciants.entrySet()) {
                            // Added this approximation because of
                            // a precision error in the last test
                            BigDecimal roundedValue = BigDecimal.valueOf(entry.getValue())
                                    .setScale(Utils.PRECISION, RoundingMode.HALF_UP);
                            ObjectNode commerciantNode = objectMapper.createObjectNode();
                            commerciantNode.put("commerciant", entry.getKey());
                            commerciantNode.put("total", roundedValue);
                            commerciantArray.add(commerciantNode);
                        }
                        result.set("commerciants", commerciantArray);
                        node.set("output", result);
                        node.put("timestamp", timestamp);
                        output.add(node);
                        return;
                    }
                }
            }
            throw new AccountNotFoundException();
        } catch (MyException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
