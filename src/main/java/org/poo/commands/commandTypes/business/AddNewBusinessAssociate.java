package org.poo.commands.commandTypes.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.Command;
import org.poo.commerciants.Commerciant;
import org.poo.core.User;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.BusinessAccount;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.exceptions.MyException;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

@Getter @Setter
public class AddNewBusinessAssociate extends Command {
    private String account;
    private String role;
    private String email;

    public AddNewBusinessAssociate(CommandInput input) {
        super(input);
        account = input.getAccount();
        role = input.getRole();
        email = input.getEmail();
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        BusinessAccount businessAccount = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.account) && account.getType().equals("business")) {
                    businessAccount = (BusinessAccount) account;
                    break;
                }
            }
        }
        if (businessAccount != null) {
            for (User user : users) {
                if (user.getEmail().equals(this.email)) {
                    if (user.getEmail().equals(businessAccount.getUser().getEmail())) {
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

