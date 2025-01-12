package org.poo.commands.commandTypes.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.user.User;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.BusinessAccount;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.*;

/**
 * Implementation for the businessReport command
 */
@Getter @Setter
public class BusinessReport extends Report {
    private String type;

    public BusinessReport(final CommandInput input) {
        super(input);
        type = input.getType();
    }

    /**
     * Prints a business report for the specified business account
     */
    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        final ArrayList<Commerciant> commerciants) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", command);
        ObjectNode result = objectMapper.createObjectNode();
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban) && account.getType().equals("business")) {
                    result.put("IBAN", iban);
                    result.put("balance", account.getBalance());
                    result.put("currency", account.getCurrency());
                    result.put("spending limit", ((BusinessAccount) account).getSpendingLimit());
                    result.put("deposit limit", ((BusinessAccount) account).getDepositLimit());
                    result.put("statistics type", type);
                    if (type.equals("transaction")) {
                        @Getter @Setter
                        class UserInfo {
                            private double spent;
                            private double deposited;

                            UserInfo(final double spent, final double deposited) {
                                this.spent = spent;
                                this.deposited = deposited;
                            }

                            @Override
                            public String toString() {
                                return spent + " " + deposited;
                            }
                        }
                        Map<User, UserInfo> employeeStatistics = new HashMap<>();
                        Map<User, UserInfo> managerStatistics = new HashMap<>();
                        for (User employee : ((BusinessAccount) account).getEmployees()) {
                            employeeStatistics.put(employee, new UserInfo(0, 0));
                        }
                        for (User manager : ((BusinessAccount) account).getManagers()) {
                            managerStatistics.put(manager, new UserInfo(0, 0));
                        }
                        for (Transaction transaction : account.getTransactions().descendingSet()) {
                            if (transaction.getTimestamp() >= startTimestamp
                                    && transaction.getTimestamp() <= endTimestamp) {
                                if (transaction.getUser() != null
                                        && transaction.getUser() != account.getUser()) {
                                    if (((BusinessAccount) account).getEmployees()
                                            .contains(transaction.getUser())) {
                                        employeeStatistics.merge(transaction.getUser(),
                                                new UserInfo(transaction.getAmount(), 0),
                                                (a, b) -> new UserInfo(a.getSpent() + b.getSpent(),
                                                        a.getDeposited() + b.getDeposited()));
                                    }
                                    if (((BusinessAccount) account).getManagers()
                                            .contains(transaction.getUser())) {
                                        managerStatistics.merge(transaction.getUser(),
                                                new UserInfo(transaction.getAmount(), 0),
                                                (a, b) -> new UserInfo(a.getSpent() + b.getSpent(),
                                                        a.getDeposited() + b.getDeposited()));
                                    }
                                }
                            }
                        }
                        for (Transaction deposit : account.getDeposits().descendingSet()) {
                            if (deposit.getTimestamp() >= startTimestamp
                                    && deposit.getTimestamp() <= endTimestamp) {
                                if (deposit.getUser() != null
                                        && deposit.getUser() != account.getUser()) {
                                    if (((BusinessAccount) account).getEmployees()
                                            .contains(deposit.getUser())) {
                                        employeeStatistics.merge(deposit.getUser(),
                                                new UserInfo(0, deposit.getAmount()),
                                                (a, b) -> new UserInfo(a.getSpent() + b.getSpent(),
                                                        a.getDeposited() + b.getDeposited()));
                                    }
                                    if (((BusinessAccount) account).getManagers()
                                            .contains(deposit.getUser())) {
                                        managerStatistics.merge(deposit.getUser(),
                                                new UserInfo(0, deposit.getAmount()),
                                                (a, b) -> new UserInfo(a.getSpent() + b.getSpent(),
                                                        a.getDeposited() + b.getDeposited()));
                                    }
                                }
                            }
                        }
                        ArrayNode managers = objectMapper.createArrayNode();
                        ArrayNode employees = objectMapper.createArrayNode();
                        double totalSpent = 0;
                        double totalDeposited = 0;
                        for (User employee : ((BusinessAccount) account).getEmployees()) {
                            employee.printBusinessUser(objectMapper, employees,
                                    employeeStatistics.get(employee).getSpent(),
                                    employeeStatistics.get(employee).getDeposited());
                            totalSpent += employeeStatistics.get(employee).getSpent();
                            totalDeposited += employeeStatistics.get(employee).getDeposited();
                        }
                        result.set("employees", employees);
                        for (User manager : ((BusinessAccount) account).getManagers()) {
                            manager.printBusinessUser(objectMapper, managers,
                                    managerStatistics.get(manager).getSpent(),
                                    managerStatistics.get(manager).getDeposited());
                            totalSpent += managerStatistics.get(manager).getSpent();
                            totalDeposited += managerStatistics.get(manager).getDeposited();
                        }
                        result.set("managers", managers);
                        result.put("total spent", totalSpent);
                        result.put("total deposited", totalDeposited);
                    } else if (type.equals("commerciant")) {
                        @Getter @Setter
                        class CommerciantInfo implements Comparable<CommerciantInfo> {
                            private String commerciant;
                            private double totalReceived;
                            private List<User> managers;
                            private List<User> employees;

                            CommerciantInfo(final String commerciant) {
                                this.commerciant = commerciant;
                                managers = new ArrayList<>();
                                employees = new ArrayList<>();
                            }

                            @Override
                            public int compareTo(final CommerciantInfo o) {
                                return this.commerciant.compareTo(o.commerciant);
                            }
                        }
                        Map<String, CommerciantInfo> commerciantInfo = new TreeMap<>();
                        ArrayNode commerciantNode = objectMapper.createArrayNode();
                        for (Transaction transaction : account.getTransactions()) {
                            if (transaction.getTimestamp() >= startTimestamp
                                    && transaction.getTimestamp() <= endTimestamp) {
                                CommerciantInfo info;
                                if (transaction.getCommerciant() != null
                                        && transaction.getUser() != account.getUser()) {
                                    if (!commerciantInfo.containsKey(transaction
                                            .getCommerciant())) {
                                        info = new CommerciantInfo(transaction.getCommerciant());
                                        if (((BusinessAccount) account).getEmployees()
                                                .contains(transaction.getUser())) {
                                            info.getEmployees().add(transaction.getUser());
                                            info.getEmployees().sort(User::compareTo);
                                        } else if (((BusinessAccount) account).getManagers()
                                                .contains(transaction.getUser())) {
                                            info.getManagers().add(transaction.getUser());
                                            info.getManagers().sort(User::compareTo);
                                        }
                                        info.setTotalReceived(transaction.getAmount());
                                        commerciantInfo.put(transaction.getCommerciant(), info);
                                    } else {
                                        info = commerciantInfo.get(transaction.getCommerciant());
                                        if (((BusinessAccount) account).getEmployees()
                                                .contains(transaction.getUser())) {
                                            info.getEmployees().add(transaction.getUser());
                                            info.getEmployees().sort(User::compareTo);
                                        } else if (((BusinessAccount) account).getManagers()
                                                .contains(transaction.getUser())) {
                                            info.getManagers().add(transaction.getUser());
                                            info.getManagers().sort(User::compareTo);
                                        }
                                        info.setTotalReceived(info.getTotalReceived()
                                                + transaction.getAmount());
                                    }
                                }
                            }
                        }
                        for (Map.Entry<String, CommerciantInfo> entry : commerciantInfo.entrySet()) {
                            ObjectNode commerciant = objectMapper.createObjectNode();
                            commerciant.put("commerciant", entry.getKey());
                            commerciant.put("total received",
                                    entry.getValue().getTotalReceived());
                            ArrayNode managers = objectMapper.createArrayNode();
                            for (User manager : entry.getValue().getManagers()) {
                                managers.add(manager.getLastName()
                                        + " " + manager.getFirstName());
                            }
                            commerciant.set("managers", managers);
                            ArrayNode employees = objectMapper.createArrayNode();
                            for (User employee : entry.getValue().getEmployees()) {
                                employees.add(employee.getLastName()
                                        + " " + employee.getFirstName());
                            }
                            commerciant.set("employees", employees);
                            commerciantNode.add(commerciant);
                        }
                        result.set("commerciants", commerciantNode);
                    }
                    node.set("output", result);
                    node.put("timestamp", timestamp);
                }
            }
        }
        output.add(node);
    }
}
