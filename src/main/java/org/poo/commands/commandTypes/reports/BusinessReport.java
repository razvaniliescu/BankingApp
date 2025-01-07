package org.poo.commands.commandTypes.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.User;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.BusinessAccount;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.Transaction;

import java.util.*;

@Getter @Setter
public class BusinessReport extends Report {
    private String type;

    public BusinessReport(CommandInput input) {
        super(input);
        type = input.getType();
    }

    @Override
    public void execute(final ObjectMapper objectMapper, final ArrayNode output,
                        final ArrayList<User> users, final ExchangeGraph rates,
                        ArrayList<Commerciant> commerciants) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", command);
        ObjectNode result = objectMapper.createObjectNode();
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban) && account.getType().equals("business")) {
                    result.put("IBAN", iban);
                    result.put("balance", Math.round(account.getBalance() * 100) / 100.0);
                    result.put("currency", account.getCurrency());
                    result.put("spending limit", Math.round(((BusinessAccount) account).getSpendingLimit() * 100) / 100.0);
                    result.put("deposit limit", Math.round(((BusinessAccount) account).getDepositLimit() * 100) / 100.0);
                    if (type.equals("transaction")) {
                        result.put("statistics type", type);
                        @Getter @Setter
                        class UserInfo {
                            private double spent;
                            private double deposited;

                            public UserInfo(final double spent, final double deposited) {
                                this.spent = spent;
                                this.deposited = deposited;
                            }
                        }
                        Map<User, UserInfo> employeeStatistics = new TreeMap<>();
                        Map<User, UserInfo> managerStatistics = new TreeMap<>();
                        for (User employee : ((BusinessAccount) account).getEmployees()) {
                            employeeStatistics.put(employee, new UserInfo(0, 0));
                        }
                        for (User manager : ((BusinessAccount) account).getManagers()) {
                            managerStatistics.put(manager, new UserInfo(0, 0));
                        }
                        for (Transaction transaction : account.getTransactions()) {
                            if (transaction.getTimestamp() >= startTimestamp && transaction.getTimestamp() <= endTimestamp) {
                                if (transaction.getUser() != null && transaction.getUser() != account.getUser()) {
                                    managerStatistics.merge(transaction.getUser(),
                                            new UserInfo(transaction.getAmount(), 0),
                                            (a, b) -> new UserInfo(a.getSpent() + b.getSpent(),
                                                    a.getDeposited() + b.getDeposited()));
                                }
                            }
                        }
                        for (Transaction deposit : account.getDeposits()) {
                            if (deposit.getTimestamp() >= startTimestamp && deposit.getTimestamp() <= endTimestamp) {
                                if (deposit.getUser() != null && deposit.getUser() != account.getUser()) {
                                    managerStatistics.merge(deposit.getUser(),
                                            new UserInfo(0, deposit.getAmount()),
                                            (a, b) -> new UserInfo(a.getSpent() + b.getSpent(),
                                                    a.getDeposited() + b.getDeposited()));
                                }
                            }
                        }
                        ArrayNode managers = objectMapper.createArrayNode();
                        ArrayNode employees = objectMapper.createArrayNode();
                        double totalSpent = 0;
                        double totalDeposited = 0;
                        for (Map.Entry<User, UserInfo> entry : employeeStatistics.entrySet()) {
                            ObjectNode employee = objectMapper.createObjectNode();
                            employee.put("username", entry.getKey().getLastName() + " " + entry.getKey().getFirstName());
                            employee.put("spent", Math.round(entry.getValue().getSpent() * 100) / 100.0);
                            totalSpent += entry.getValue().getSpent();
                            employee.put("deposited", Math.round(entry.getValue().getDeposited() * 100) / 100.0);
                            totalDeposited += entry.getValue().getDeposited();
                            employees.add(employee);
                        }
                        result.set("employees", employees);
                        for (Map.Entry<User, UserInfo> entry : managerStatistics.entrySet()) {
                            ObjectNode manager = objectMapper.createObjectNode();
                            manager.put("username", entry.getKey().getLastName() + " " + entry.getKey().getFirstName());
                            manager.put("spent", Math.round(entry.getValue().getSpent() * 100) / 100.0);
                            totalSpent += entry.getValue().getSpent();
                            manager.put("deposited", Math.round(entry.getValue().getDeposited() * 100) / 100.0);
                            totalDeposited += entry.getValue().getDeposited();
                            managers.add(manager);
                        }
                        result.set("managers", managers);
                        result.put("total spent", totalSpent);
                        result.put("total deposited", totalDeposited);
                    } else if (type.equals("commerciant")) {
                        @Getter @Setter
                        class CommerciantInfo implements Comparable<CommerciantInfo> {
                            private String commerciant;
                            private double totalReceived;
                            private Set<User> managers;
                            private Set<User> employees;

                            public CommerciantInfo(final String commerciant) {
                                this.commerciant = commerciant;
                                managers = new HashSet<>();
                                employees = new HashSet<>();
                            }

                            @Override
                            public int compareTo(CommerciantInfo o) {
                                return this.commerciant.compareTo(o.commerciant);
                            }
                        }
                        Map<String, CommerciantInfo> commerciantInfo = new TreeMap<>();
                        ArrayNode commerciantNode = objectMapper.createArrayNode();
                        for (Transaction transaction : account.getTransactions()) {
                            if (transaction.getTimestamp() >= startTimestamp && transaction.getTimestamp() <= endTimestamp) {
                                CommerciantInfo info;
                                if (transaction.getCommerciant() != null) {
                                    if (!commerciantInfo.containsKey(transaction.getCommerciant())) {
                                        info = new CommerciantInfo(transaction.getCommerciant());
                                        if (((BusinessAccount) account).getEmployees().contains(transaction.getUser())) {
                                            info.getEmployees().add(transaction.getUser());
                                        } else if (((BusinessAccount) account).getManagers().contains(transaction.getUser())) {
                                            info.getManagers().add(transaction.getUser());
                                        }
                                        info.setTotalReceived(transaction.getAmount());
                                    } else {
                                        info = commerciantInfo.get(transaction.getCommerciant());
                                        if (((BusinessAccount) account).getEmployees().contains(transaction.getUser())) {
                                            info.getEmployees().add(transaction.getUser());
                                        } else if (((BusinessAccount) account).getManagers().contains(transaction.getUser())) {
                                            info.getManagers().add(transaction.getUser());
                                        }
                                        info.setTotalReceived(info.getTotalReceived() + transaction.getAmount());
                                    }
                                    commerciantInfo.put(transaction.getCommerciant(), info);
                                }
                            }
                        }
                        for (Map.Entry<String, CommerciantInfo> entry : commerciantInfo.entrySet()) {
                            ObjectNode commerciant = objectMapper.createObjectNode();
                            commerciant.put("commerciant", entry.getKey());
                            commerciant.put("totalReceived", entry.getValue().getTotalReceived());
                            ArrayNode managers = objectMapper.createArrayNode();
                            for (User manager : entry.getValue().getManagers()) {
                                managers.add(manager.getLastName() + " " + manager.getFirstName());
                            }
                            commerciant.set("managers", managers);
                            ArrayNode employees = objectMapper.createArrayNode();
                            for (User employee : entry.getValue().getEmployees()) {
                                employees.add(employee.getLastName() + " " + employee.getFirstName());
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
