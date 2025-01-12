package org.poo.commands.commandTypes.account.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.Command;
import org.poo.commerciants.Commerciant;
import org.poo.core.user.User;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.BusinessAccount;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

/**
 * Implementation for the addNewBusinessAssociate command
 */
@Getter @Setter
public class AddNewBusinessAssociate extends Command {
    private String account;
    private String role;
    private String email;

    public AddNewBusinessAssociate(final CommandInput input) {
        super(input);
        account = input.getAccount();
        role = input.getRole();
        email = input.getEmail();
    }

    /**
     * Adds a new associate to the specified business account
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        BusinessAccount businessAccount = null;
        for (User user : users) {
            for (Account acc : user.getAccounts()) {
                if (acc.getIban().equals(this.account)
                        && acc.getType().equals("business")) {
                    businessAccount = (BusinessAccount) acc;
                    break;
                }
            }
        }
        if (businessAccount != null) {
            for (User user : users) {
                if (user.getEmail().equals(this.email)) {
                    if (user.getEmail().equals(businessAccount
                            .getUser().getEmail())) {
                        return;
                    }
                    user.addAccount(businessAccount);
                    if (businessAccount.getManagers().contains(user)
                            || businessAccount.getEmployees().contains(user)
                            || businessAccount.getUser().equals(user)) {
                        return;
                    }
                    if (role.equals("manager")) {
                        businessAccount.addManager(user);
                    } else if (role.equals("employee")) {
                        businessAccount.addEmployee(user);
                    }
                    return;
                }
            }
        }
    }
}

