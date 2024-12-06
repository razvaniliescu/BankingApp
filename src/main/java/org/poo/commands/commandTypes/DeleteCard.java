package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.transactions.DelCardTransaction;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class DeleteCard extends Command {
    private String card;
    private ArrayList<User> users;
    private String email;
    private String iban;

    public DeleteCard(CommandInput input, ArrayList<User> users) {
        this.users = users;
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
        this.card = input.getCardNumber();
        this.email = input.getEmail();
        this.iban = input.getAccount();
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
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

    public void deleteCard() {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(this.card)) {
                        this.iban = account.getIban();
                        account.deleteCard(card);
                        user.addTransaction(new DelCardTransaction(this));
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode, ArrayList<Transaction> transactions) {
        deleteCard();
    }
}
