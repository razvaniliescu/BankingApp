package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.NewCard;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Objects;

public class CreateCard extends Command {
    protected ArrayList<User> users;
    protected String iban;
    protected Card card;
    protected String email;

    public CreateCard(CommandInput input, ArrayList<User> users) {
        this.users = users;
        this.email = input.getEmail();
        this.iban = input.getAccount();
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void createCard() {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    if (user.getEmail().equals(email)) {
                        this.card = new Card();
                        account.addCard(card);
                        user.addTransaction(new NewCard(this));
                        return;
                    }

                }
            }
        }
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode, ArrayList<Transaction> transactions) {
        createCard();
    }
}
