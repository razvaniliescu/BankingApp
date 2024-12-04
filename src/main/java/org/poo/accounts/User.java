package org.poo.accounts;

import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;

    public User(UserInput user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        accounts = new ArrayList<>();
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

    public void addAccount(Account account) {
        accounts.add(account);
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
        accounts.remove(account);
    }
}
