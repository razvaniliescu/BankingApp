package org.poo.commands.commandTypes.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.Command;
import org.poo.core.ServicePlans;
import org.poo.core.User;
import org.poo.core.accounts.Account;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

@Getter @Setter
public class UpgradePlan extends Command {
    private String plan;
    private String iban;

    public UpgradePlan(CommandInput input) {
        super(input);
        this.plan = input.getNewPlanType();
        this.iban = input.getAccount();
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates) {
        ServicePlans.Plans plan = ServicePlans.getPlan(this.plan);
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.getIban())) {
                    if (plan == account.getPlan()) {

                    }

                }
            }
        }

    }
}
