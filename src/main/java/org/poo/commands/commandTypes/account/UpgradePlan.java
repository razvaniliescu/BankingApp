package org.poo.commands.commandTypes.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.Command;
import org.poo.commerciants.Commerciant;
import org.poo.core.user.ServicePlans;
import org.poo.core.user.User;
import org.poo.core.accounts.Account;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.exceptions.MyException;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

/**
 * Implementation for the upgradePlan command
 */
@Getter @Setter
public class UpgradePlan extends Command {
    private String plan;
    private String iban;

    public UpgradePlan(final CommandInput input) {
        super(input);
        this.plan = input.getNewPlanType();
        this.iban = input.getAccount();
    }

    /**
     * Upgrades the user's current plan
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        try {
            ServicePlans.Plans servicePlan = ServicePlans.getPlan(this.plan);
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(this.getIban())) {
                        if (servicePlan == account.getPlan()) {
                            Transaction t = new Transaction.Builder(timestamp,
                                    "The user already has the "
                                            + servicePlan.toString() + " plan.")
                                    .build();
                            account.addTransaction(t);
                            user.addTransaction(t);
                            return;
                        }
                        int gradeDif = ServicePlans.getPlanGrade(servicePlan)
                                - ServicePlans.getPlanGrade(account.getPlan());
                        if (gradeDif <= 0) {
                            Transaction t = new Transaction.Builder(timestamp,
                                    "You cannot downgrade your plan")
                                    .iban(iban)
                                    .newPlanType(servicePlan)
                                    .build();
                            account.addTransaction(t);
                            user.addTransaction(t);
                            return;
                        }
                        double fee = switch (gradeDif) {
                            case Utils.SILVER_PLAN_VALUE -> Utils.SILVER_FEE;
                            case Utils.GOLD_PLAN_VALUE - Utils.SILVER_PLAN_VALUE -> Utils.GOLD_FEE;
                            case Utils.GOLD_PLAN_VALUE -> Utils.SILVER_FEE + Utils.GOLD_FEE;
                            default -> 0;
                        };
                        fee *= rates.getExchangeRate("RON", account.getCurrency());
                        if (account.getBalance() - fee < account.getMinBalance()) {
                            Transaction t = new Transaction.Builder(timestamp,
                                    "Insufficient funds")
                                    .build();
                            account.addTransaction(t);
                            user.addTransaction(t);
                            return;
                        }
                        account.setBalance(account.getBalance() - fee);
                        user.setBasePlan(servicePlan);
                        for (Account acc : user.getAccounts()) {
                            if (acc.getType().equals("business") && !acc.getUser().equals(user)) {
                                continue;
                            }
                            acc.setPlan(servicePlan);
                        }
                        Transaction t = new Transaction.Builder(timestamp, "Upgrade plan")
                                .iban(iban)
                                .newPlanType(servicePlan)
                                .build();
                        account.addTransaction(t);
                        user.addTransaction(t);
                        return;
                    }
                }
            }
            throw new MyException("Account not found");
        } catch (MyException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
