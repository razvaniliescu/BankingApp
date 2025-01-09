package org.poo.core.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.User;
import org.poo.core.exchange.ExchangeGraph;

import java.util.*;

@Getter @Setter
public class BusinessAccount extends Account {
    private List<User> employees;
    private List<User> managers;
    private double spendingLimit;
    private double depositLimit;
    private Map<String, Commerciant> commerciantAliases;

    public BusinessAccount(String currency, String type, User user, ExchangeGraph rates) {
        super(currency, type, user);
        this.employees = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.commerciantAliases = new HashMap<>();
        this.spendingLimit = 500 * rates.getExchangeRate("RON", currency);
        this.depositLimit = 500 * rates.getExchangeRate("RON", currency);
    }

    public void addManager(User user) {
        managers.add(user);
    }

    public void removeManager(User user) {
        managers.remove(user);
    }

    public void addEmployee(User user) {
        employees.add(user);
    }

    public void removeEmployee(User user) {
        employees.remove(user);
    }

    public boolean isInvolvedInAccount(User user) {
        return employees.contains(user) || managers.contains(user) || this.user.equals(user);
    }
}
