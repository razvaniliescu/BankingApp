package org.poo.commands.commandTypes.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.Command;
import org.poo.commerciants.Commerciant;
import org.poo.core.ServicePlans;
import org.poo.core.User;
import org.poo.core.accounts.Account;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

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
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        ServicePlans.Plans plan = ServicePlans.getPlan(this.plan);
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(this.getIban())) {
                    if (plan == account.getPlan()) {
                        Transaction t = new Transaction.Builder(timestamp, "The user already has the " + plan.toString() + " plan.")
                                .iban(iban)
                                .newPlanType(plan)
                                .build();
                        account.addTransaction(t);
                        user.addTransaction(t);
                        return;
                    }
                    int gradeDif = ServicePlans.getPlanGrade(plan) - ServicePlans.getPlanGrade(account.getPlan());
                    if (gradeDif <= 0) {
                        Transaction t = new Transaction.Builder(timestamp, "You cannot downgrade your plan")
                                .iban(iban)
                                .newPlanType(plan)
                                .build();
                        account.addTransaction(t);
                        user.addTransaction(t);
                        return;
                    }
                    double fee = switch (gradeDif) {
                        case 1 -> 100;
                        case 2 -> 250;
                        case 3 -> 350;
                        default -> 0;
                    };
                    fee *= rates.getExchangeRate("RON", account.getCurrency());
                    if (account.getBalance() - fee < account.getMinBalance()) {
                        Transaction t = new Transaction.Builder(timestamp, "Insufficient funds")
                                .iban(iban)
                                .newPlanType(plan)
                                .build();
                        account.addTransaction(t);
                        user.addTransaction(t);
                        return;
                    }
                    account.setBalance(account.getBalance() - fee);
                    for (Account acc : user.getAccounts()) {
                        acc.setPlan(plan);
                    }
                    Transaction t = new Transaction.Builder(timestamp, "Upgrade plan")
                            .iban(iban)
                            .newPlanType(plan)
                            .build();
                    account.addTransaction(t);
                    user.addTransaction(t);
                    return;
                }
            }
        }

    }
}
