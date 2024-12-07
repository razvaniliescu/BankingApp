package org.poo.accounts;

import org.poo.fileio.UserInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;
    private ArrayList<SavingsAccount> savingsAccounts;
    private Map<String, Account> aliases;
    private ArrayList<Transaction> transactions;

    public User(UserInput user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        accounts = new ArrayList<>();
        savingsAccounts = new ArrayList<>();
        aliases = new HashMap<>();
        transactions = new ArrayList<>();
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Map<String, Account> getAliases() {
        return aliases;
    }

    public void setAliases(Map<String, Account> aliases) {
        this.aliases = aliases;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public ArrayList<SavingsAccount> getSavingsAccounts() {
        return savingsAccounts;
    }

    public void setSavingsAccounts(ArrayList<SavingsAccount> savingsAccounts) {
        this.savingsAccounts = savingsAccounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void addSavingsAccount(SavingsAccount savingsAccount) {
        savingsAccounts.add(savingsAccount);
    }

    public Account getAccount(String iban) {
        for (Account account : accounts) {
            if (account.getIban().equals(iban)) {
                return account;
            }
        }
        throw new IllegalArgumentException("No such account");
    }

    public void deleteAccount(Account account) {
        if (account.getBalance() == 0) {
            accounts.remove(account);
        } else {
            throw new IllegalArgumentException("Account couldn't be deleted - see org.poo.transactions for details");
        }
    }

    public void addAlias(String alias, Account account) {
        aliases.put(alias, account);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
