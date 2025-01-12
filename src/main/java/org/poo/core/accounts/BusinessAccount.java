package org.poo.core.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.core.user.User;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.utils.Utils;

import java.util.*;

/**
 * Class for the business account
 */
@Getter @Setter
public class BusinessAccount extends Account {
    private List<User> employees;
    private List<User> managers;
    private double spendingLimit;
    private double depositLimit;
    private Map<String, Commerciant> commerciantAliases;

    public BusinessAccount(final String currency, final String type,
                           final User user, final ExchangeGraph rates) {
        super(currency, type, user);
        this.employees = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.commerciantAliases = new HashMap<>();
        this.spendingLimit = Utils.INITIAL_LIMIT * rates.getExchangeRate("RON", currency);
        this.depositLimit = Utils.INITIAL_LIMIT * rates.getExchangeRate("RON", currency);
    }

    /**
     * Adds a manager to the account
     */
    public void addManager(final User user) {
        managers.add(user);
    }

    /**
     * Adds an employee to the account
     */
    public void addEmployee(final User user) {
        employees.add(user);
    }
}
