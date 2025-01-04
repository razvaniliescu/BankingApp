package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.core.accounts.Account;

import java.util.List;


/**
 * The base form of a transaction, which contains only
 * a timestamp and a description
 */
@Setter
@Getter
public class Transaction {
    protected int timestamp;
    protected String description;
    private String senderIBAN;
    private String receiverIBAN;
    private double amount;
    private String currency;
    private String type;
    private String commerciant;
    private String iban;
    private String card;
    private String email;
    private String cardHolder;
    private String account;
    private List<Account> accounts;
    private String errorMessage;


    public static class Builder {
        protected int timestamp;
        protected String description;
        private String senderIBAN = null;
        private String receiverIBAN = null;
        private double amount = 0.0;
        private String currency = null;
        private String type = null;
        private String commerciant = null;
        private String iban = null;
        private String card = null;
        private String email = null;
        private String cardHolder = null;
        private String account = null;
        private List<Account> accounts = null;
        private String errorMessage = null;

        public Builder(int timestamp, String description) {
            this.timestamp = timestamp;
            this.description = description;
        }

        public Builder senderIBAN(String senderIBAN) {
            this.senderIBAN = senderIBAN;
            return this;
        }

        public Builder receiverIBAN(String receiverIBAN) {
            this.receiverIBAN = receiverIBAN;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder commerciant(String commerciant) {
            this.commerciant = commerciant;
            return this;
        }

        public Builder iban(String iban) {
            this.iban = iban;
            return this;
        }

        public Builder card(String card) {
            this.card = card;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder cardHolder(String cardHolder) {
            this.cardHolder = cardHolder;
            return this;
        }

        public Builder account(String account) {
            this.account = account;
            return this;
        }

        public Builder accounts(List<Account> accounts) {
            this.accounts = accounts;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }

    private Transaction(Builder builder) {
        this.timestamp = builder.timestamp;
        this.description = builder.description;
        this.senderIBAN = builder.senderIBAN;
        this.receiverIBAN = builder.receiverIBAN;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.type = builder.type;
        this.commerciant = builder.commerciant;
        this.iban = builder.iban;
        this.card = builder.card;
        this.email = builder.email;
        this.cardHolder = builder.cardHolder;
        this.account = builder.account;
        this.accounts = builder.accounts;
        this.errorMessage = builder.errorMessage;
    }

    /**
     * Adds the transaction to the output in JSON format
     */
    public void print(final ObjectMapper objectMapper, final ArrayNode arrayNode) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", description);
        if (senderIBAN != null) {
            node.put("senderIBAN", senderIBAN);
        }
        if (receiverIBAN != null) {
            node.put("receiverIBAN", receiverIBAN);
        }
        if (amount != 0.0) {
            node.put("amount", amount);
        }
        if (currency != null) {
            node.put("currency", currency);
        }
        if (type != null) {
            node.put("type", type);
        }
        if (commerciant != null) {
            node.put("commerciant", commerciant);
        }
        if (iban != null) {
            node.put("iban", iban);
        }
        if (card != null) {
            node.put("card", card);
        }
        if (email != null) {
            node.put("email", email);
        }
        if (cardHolder != null) {
            node.put("cardHolder", cardHolder);
        }
        if (account != null) {
            node.put("account", account);
        }
        if (accounts != null) {
            ArrayNode accountArray = objectMapper.createArrayNode();
            for (Account account : accounts) {
                accountArray.add(account.getIban());
            }
            node.set("involvedAccounts", accountArray);
        }
        if (errorMessage != null) {
            node.put("error", errorMessage);
        }
        arrayNode.add(node);
    }
}
