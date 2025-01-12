package org.poo.commands.commandTypes.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.accounts.Account;
import org.poo.core.user.User;
import org.poo.commands.Command;
import org.poo.core.accounts.BusinessAccount;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the addFunds command
 */
@Setter
@Getter
public class AddFunds extends Command {
    private String iban;
    private double funds;
    private String email;

    public AddFunds(final CommandInput input) {
        super(input);
        this.iban = input.getAccount();
        this.funds = input.getAmount();
        this.email = input.getEmail();
    }

    /**
     * Finds the specified account and
     * adds funds to it
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode arrayNode,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        User userAddingFunds = null;
        for (User user : users) {
            if (user.getEmail().equals(this.email)) {
                userAddingFunds = user;
            }
        }
        if (userAddingFunds == null) {
            return;
        }
        Set<Account> accounts = users.stream().
                map(User::getAccounts)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        for (Account account : accounts) {
            if (account.getIban().equals(iban)) {
                if (account.getType().equals("classic") || account.getType().equals("savings")) {
                    account.addFunds(this.funds);
                    return;
                } else if (account.getType().equals("business")) {
                    if (((BusinessAccount) account).getEmployees().contains(userAddingFunds)) {
                        if (((BusinessAccount) account).getDepositLimit() >= this.funds) {
                            account.addFunds(this.funds);
                            account.addDeposit(new Transaction.Builder(timestamp, "Deposit")
                                    .amount(funds)
                                    .user(userAddingFunds)
                                    .build());
                        }
                        return;
                    } else if (account.getUser().getEmail().equals(userAddingFunds.getEmail())
                        || ((BusinessAccount) account).getManagers().contains(userAddingFunds)) {
                        account.addFunds(this.funds);
                        account.addDeposit(new Transaction.Builder(timestamp, "Deposit")
                                .amount(funds)
                                .user(userAddingFunds)
                                .build());
                        return;
                    }
                }
            }
        }
    }
}
