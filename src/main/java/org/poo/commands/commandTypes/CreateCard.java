package org.poo.commands.commandTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.accounts.Account;
import org.poo.accounts.Card;
import org.poo.accounts.User;
import org.poo.commands.Command;
import org.poo.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;
import org.poo.transactions.NewCard;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Objects;

public class CreateCard extends Command {
    protected String iban;
    protected Card card;
    protected String email;

    public CreateCard(CommandInput input) {
        super(input);
        this.email = input.getEmail();
        this.iban = input.getAccount();
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
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

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode arrayNode, ArrayList<User> users, ExchangeGraph rates) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    if (user.getEmail().equals(email)) {
                        this.card = new Card();
                        account.addCard(card);
                        Transaction t = new NewCard(this);
                        user.addTransaction(t);
                        account.addTransaction(t);
                        return;
                    }

                }
            }
        }
    }
}
