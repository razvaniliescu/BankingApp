package org.poo.commands.commandTypes.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.BusinessAccount;
import org.poo.core.accounts.SavingsAccount;
import org.poo.core.user.User;
import org.poo.commands.Command;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Implementation of the addAccount command
 */
@Setter
@Getter
public class AddAccount extends Command {
    private String user;
    private String currency;
    private String type;
    private double interestRate;

    public AddAccount(final CommandInput input) {
        super(input);
        this.user = input.getEmail();
        this.currency = input.getCurrency();
        this.type = input.getAccountType();
        this.interestRate = input.getInterestRate();
    }

    /**
     * Finds the user with the specified email
     * and adds an account
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode arrayNode,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        for (User u : users) {
            if (Objects.equals(u.getEmail(), this.user)) {
                Account account = null;
                if (Objects.equals(type, "classic")) {
                    account = new Account(currency, type, u);
                } else if (Objects.equals(type, "savings")) {
                    account = new SavingsAccount(currency, type, interestRate, u);
                    u.addSavingsAccount((SavingsAccount) account);
                } else if (Objects.equals(type, "business")) {
                    account = new BusinessAccount(currency, type, u, rates);
                }
                u.addAccount(account);
                Transaction t = new Transaction.Builder(timestamp, "New account created")
                        .build();
                u.addTransaction(t);
                if (account != null) {
                    account.addTransaction(t);
                }
            }
        }
    }
}
