package org.poo.core;

import lombok.Getter;
import lombok.Setter;
import org.poo.core.accounts.Account;
import org.poo.core.accounts.SavingsAccount;
import org.poo.core.cards.Card;
import org.poo.exceptions.BalanceNotEmptyException;
import org.poo.exceptions.CardNotFoundException;
import org.poo.exceptions.SavingsAccountException;
import org.poo.fileio.UserInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for user elements and operations
 */
@Setter
@Getter
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;
    private ArrayList<SavingsAccount> savingsAccounts;
    private Map<String, Account> aliases;
    private ArrayList<Transaction> transactions;

    public User(final UserInput user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        accounts = new ArrayList<>();
        savingsAccounts = new ArrayList<>();
        aliases = new HashMap<>();
        transactions = new ArrayList<>();
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
     * @throws BalanceNotEmptyException if the account isn't empty
     */
    public void deleteAccount(final Account account) throws BalanceNotEmptyException {
        if (account.getBalance() == 0) {
            accounts.remove(account);
        } else {
            throw new BalanceNotEmptyException();
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
    }

    /**
     * Tries to find the savings account
     * @param iban the accounts IBAN
     * @throws SavingsAccountException if the account doesn't exist
     */
    public SavingsAccount checkSavingsAccounts(final String iban) throws SavingsAccountException {
        for (SavingsAccount savingsAccount : savingsAccounts) {
            if (savingsAccount.getIban().equals(iban)) {
                return savingsAccount;
            }
        }
        throw new SavingsAccountException();
    }

    /**
     * Tries to find the card
     * @param cardNumber the card to find
     * @return the account linked to the card
     * @throws CardNotFoundException if the card doesn't exist
     */
    public Account checkCard(final String cardNumber) throws CardNotFoundException {
        for (Account account : accounts) {
            for (Card card : account.getCards()) {
                if (card.getCardNumber().equals(cardNumber)) {
                    return account;
                }
            }
        }
        throw new CardNotFoundException();
    }
}
