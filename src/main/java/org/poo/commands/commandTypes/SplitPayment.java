package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.SplitPayTransaction;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitPayment extends Command {
    private List<String> accounts;
    private double amount;
    private String currency;

    public SplitPayment(CommandInput input) {
        super(input);
        this.accounts = input.getAccounts();
        this.amount = input.getAmount();
        this.currency = input.getCurrency();
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates) {
        boolean allCanPay = true;
        int accountsNum = accounts.size();
        Map<Account, User> userAccountMap = new HashMap<>();
        ArrayList<Account> accounts = new ArrayList<>();
        for (String iban: this.accounts) {
            for (User user: users) {
                for (Account account: user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        accounts.add(account);
                        userAccountMap.put(account, user);
                        if (!account.canPay(amount / accountsNum, currency, rates)) {
                            allCanPay = false;
                        }
                    }
                }
            }
        }
        if (allCanPay) {
            for (Account account: accounts) {
                Transaction t = new SplitPayTransaction(this);
                account.pay(amount / accountsNum, currency, rates);
                userAccountMap.get(account).addTransaction(new SplitPayTransaction(this));
                account.addTransaction(t);
            }
        } else {

        }
    }
}
