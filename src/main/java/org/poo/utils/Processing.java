package org.poo.utils;

import org.poo.accounts.Account;
import org.poo.accounts.User;

import java.util.ArrayList;

public class Processing {
    public static User findUser(String email, ArrayList<User> users) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public static Account findAccount(User user, String iban) {
        for (Account account : user.getAccounts()) {
            if (account.getIban().equals(iban)) {
                return account;
            }
        }
        return null;
    }


    public static Account getAccountFromAlias(User user, String alias) {
        return user.getAliases().get(alias);
    }
}
