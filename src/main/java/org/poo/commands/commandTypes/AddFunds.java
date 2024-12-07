package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Objects;

public class AddFunds extends Command {
    private String account;
    private double funds;

    public AddFunds(CommandInput input) {
        super(input);
        this.account = input.getAccount();
        this.funds = input.getAmount();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getFunds() {
        return funds;
    }

    public void setFunds(double funds) {
        this.funds = funds;
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode, ArrayList<User> users, ExchangeGraph rates) {
        for (User user: users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.account)) {
                    account.addFunds(this.funds);
                    return;
                }
            }
        }
    }
}
