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
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

@Getter @Setter
public class ChangeDepositLimit extends Command {
    private String email;
    private String account;
    private double amount;

    public ChangeDepositLimit(CommandInput input) {
        super(input);
        email = input.getEmail();
        account = input.getAccount();
        amount = input.getAmount();
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.account) && account.getType().equals("business")) {
                    if (user.equals(account.getUser())) {
                        ((BusinessAccount) account).setDepositLimit(amount);
                        return;
                    } else {
//                        user.addTransaction(new Transaction.Builder(timestamp,
//                                "You are not authorized to make this transaction.")
//                                .build());
                    }
                }
            }
        }
    }
}
