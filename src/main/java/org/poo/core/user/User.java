package org.poo.core.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.commands.commandTypes.payments.splitPayment.SplitPayment;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.SavingsAccount;
import org.poo.exceptions.MyException;
import org.poo.fileio.UserInput;
import org.poo.transactions.Transaction;

import java.util.*;

import static org.poo.utils.Utils.CURRENT_YEAR;

/**
 * Class for user elements and operations
 */
@Setter
@Getter
public class User implements Comparable<User> {
    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private String occupation;
    private List<Account> accounts;
    private List<SavingsAccount> savingsAccounts;
    private Map<String, Account> aliases;
    private List<Transaction> transactions;
    private ServicePlans.Plans basePlan;
    private List<SplitPayment> pendingPayments;

    public User(final UserInput user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
        this.occupation = user.getOccupation();
        accounts = new ArrayList<>();
        savingsAccounts = new ArrayList<>();
        aliases = new HashMap<>();
        transactions = new ArrayList<>();
        pendingPayments = new ArrayList<>();
        if (occupation.equals("student")) {
            basePlan = ServicePlans.Plans.student;
        } else {
            basePlan = ServicePlans.Plans.standard;
        }
    }

    /**
     * Adds an account to the user's account list
     * @param account the account to be added
     */
    public void addAccount(final Account account) {
        accounts.add(account);
    }

    /**
     * Adds a savings account to the user's savings account list
     * @param savingsAccount the account to be added
     */
    public void addSavingsAccount(final SavingsAccount savingsAccount) {
        savingsAccounts.add(savingsAccount);
    }

    /**
     * Removes an account from the user's account list
     * @param account the account to be removed
     * @throws MyException if the account isn't empty
     */
    public void deleteAccount(final Account account) throws MyException {
        if (account.getBalance() == 0) {
            accounts.remove(account);
            if (account.getType().equals("savings")) {
                savingsAccounts.remove(account);
            }
        } else {
            throw new MyException("Account couldn't be deleted "
                    + "- see org.poo.transactions for details");
        }
    }

    /**
     * Adds an alias to one of the user's accounts
     * @param alias the alias of the account
     * @param account the account in question
     */
    public void addAlias(final String alias, final Account account) {
        aliases.put(alias, account);
    }

    /**
     * Adds a new transaction to the user's transaction list
     * @param transaction the transaction to be added
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
        transactions.sort(Comparator.comparing(Transaction::getTimestamp));
    }

    /**
     * Calculates the age of the user
     */
    public int getAge() {
        String[] ymd = birthDate.split("-");
        int year = Integer.parseInt(ymd[0]);
        return CURRENT_YEAR - year;
    }

    /**
     * Compares this user to another
     * based on their first name
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(final User o) {
        return this.getFirstName().compareTo(o.getFirstName());
    }

    /**
     * Prints the user in a format specific
     * to the transaction business report
     */
    public void printBusinessUser(final ObjectMapper objectMapper, final ArrayNode output,
                                  final double spent, final double deposited) {
        ObjectNode userNode = objectMapper.createObjectNode();
        userNode.put("username", getLastName() + " " + getFirstName());
        userNode.put("spent", spent);
        userNode.put("deposited", deposited);
        output.add(userNode);
    }
}
