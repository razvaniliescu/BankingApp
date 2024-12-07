package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.SavingsAccount;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Objects;

public class ChangeInterestRate extends Command {
    private String iban;
    private double interestRate;

    public ChangeInterestRate(CommandInput input) {
        super(input);
        this.iban = input.getAccount();
        this.interestRate = input.getInterestRate();
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates) {
        try {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        if (Objects.equals(account.getType(), "savings")) {
                            ((SavingsAccount)account).setInterestRate(interestRate);
                            user.addTransaction(new Transaction(timestamp,
                                    "Interest rate of the account changed to " + interestRate));
                        } else {
                            throw new IllegalArgumentException("This is not a savings account");
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("command", command);
            ObjectNode result = objectMapper.createObjectNode();
            result.put("timestamp", timestamp);
            result.put("description", e.getMessage());
            node.set("output", result);
            node.put("timestamp", timestamp);
            output.add(node);
        }
    }
}
