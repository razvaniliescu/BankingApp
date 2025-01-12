package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.core.user.ServicePlans;
import org.poo.core.user.User;
import org.poo.core.accounts.Account;
import org.poo.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


/**
 * The class for transactions, containing all
 * possible fields that a transaction can have
 */
@Setter
@Getter
public final class Transaction implements Comparable<Transaction> {
    private int timestamp;
    private String description;
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
    private ServicePlans.Plans planType;
    private boolean currencyFormat;
    private List<Double> amountForUsers;
    private String splitPaymentType;
    private String splitPaymentCurrency;
    private User user;
    private boolean deposit;
    private List<String> accountsInvolved;
    private String classicAccountIban;
    private String savingsAccountIban;

    /**
     * Compares this transaction to another one
     * based on the timestamp
     */
    @Override
    public int compareTo(final Transaction o) {
        return this.timestamp - o.timestamp;
    }

    /**
     * Builder for the transaction class
     */
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
        private ServicePlans.Plans planType = null;
        private boolean currencyFormat = false;
        private List<Double> amountForUsers = null;
        private String splitPaymentType = null;
        private String splitPaymentCurrency = null;
        private User user = null;
        private boolean deposit = false;
        private List<String> accountsInvolved = null;
        private String classicAccountIban = null;
        private String savingsAccountIban = null;

        /**
         * Constructor for the builder containing the
         * mandatory fields for a transaction
         */
        public Builder(final int timestamp, final String description) {
            this.timestamp = timestamp;
            this.description = description;
        }

        /**
         * Sets the sender IBAN field
         */
        public final Builder senderIBAN(final String newSenderIBAN) {
            this.senderIBAN = newSenderIBAN;
            return this;
        }

        /**
         * Sets the receiver IBAN field
         */
        public Builder receiverIBAN(final String newReceiverIBAN) {
            this.receiverIBAN = newReceiverIBAN;
            return this;
        }

        /**
         * Sets the amount field
         */
        public Builder amount(final double newAmount) {
            this.amount = newAmount;
            return this;
        }

        /**
         * Sets the currency field
         */
        public Builder currency(final String newCurrency) {
            this.currency = newCurrency;
            return this;
        }

        /**
         * Sets the type field for the send money command
         * (sent or received)
         */
        public Builder type(final String newType) {
            this.type = newType;
            return this;
        }

        /**
         * Sets the commerciant field
         */
        public Builder commerciant(final String newCommerciant) {
            this.commerciant = newCommerciant;
            return this;
        }

        /**
         * Sets the IBAN field
         */
        public Builder iban(final String newIban) {
            this.iban = newIban;
            return this;
        }

        /**
         * Sets the card field
         */
        public Builder card(final String newCard) {
            this.card = newCard;
            return this;
        }

        /**
         * Sets the email field
         */
        public Builder email(final String newEmail) {
            this.email = newEmail;
            return this;
        }

        /**
         * Sets the cardholder field
         */
        public Builder cardHolder(final String newCardHolder) {
            this.cardHolder = newCardHolder;
            return this;
        }

        /**
         * Sets the account field
         */
        public Builder account(final String newAccount) {
            this.account = newAccount;
            return this;
        }

        /**
         * Sets the accounts field
         */
        public Builder accounts(final List<Account> newAccounts) {
            this.accounts = newAccounts;
            return this;
        }

        /**
         * Sets the error field
         */
        public Builder errorMessage(final String newErrorMessage) {
            this.errorMessage = newErrorMessage;
            return this;
        }

        /**
         * Sets the service plan field
         */
        public Builder newPlanType(final ServicePlans.Plans newPlanType) {
            this.planType = newPlanType;
            return this;
        }

        /**
         * Changes the way the amount and currency is printed
         */
        public Builder currencyFormat(final boolean newCurrencyFormat) {
            this.currencyFormat = newCurrencyFormat;
            return this;
        }

        /**
         * Sets the amount for users field
         */
        public Builder amountForUsers(final List<Double> newAmountForUsers) {
            this.amountForUsers = newAmountForUsers;
            return this;
        }

        /**
         * Sets the split payment type field
         */
        public Builder splitPaymentType(final String newSplitPaymentType) {
            this.splitPaymentType = newSplitPaymentType;
            return this;
        }

        /**
         * Sets the split payment currency field
         */
        public Builder splitPaymentCurrency(final String newSplitPaymentCurrency) {
            this.splitPaymentCurrency = newSplitPaymentCurrency;
            return this;
        }

        /**
         * Sets the user field (used for business reports)
         */
        public Builder user(final User newUser) {
            this.user = newUser;
            return this;
        }

        /**
         * Sets the deposit field (used for business reports)
         */
        public Builder deposit(final boolean newDeposit) {
            this.deposit = newDeposit;
            return this;
        }

        /**
         * Sets the accounts involved field
         */
        public Builder accountsInvolved(final List<String> newAccountsInvolved) {
            this.accountsInvolved = newAccountsInvolved;
            return this;
        }

        /**
         * Sets the classic account IBAN field (withdraw savings)
         */
        public Builder classicAccountIban(final String newClassicAccountIban) {
            this.classicAccountIban = newClassicAccountIban;
            return this;
        }

        /**
         * Sets the savings account IBAN field (withdraw savings)
         */
        public Builder savingsAccountIban(final String newSavingsAccountIban) {
            this.savingsAccountIban = newSavingsAccountIban;
            return this;
        }

        /**
         * Builds a new transaction based on the set fields
         */
        public Transaction build() {
            return new Transaction(this);
        }
    }

    private Transaction(final Builder builder) {
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
        this.planType = builder.planType;
        this.currencyFormat = builder.currencyFormat;
        this.amountForUsers = builder.amountForUsers;
        this.splitPaymentType = builder.splitPaymentType;
        this.splitPaymentCurrency = builder.splitPaymentCurrency;
        this.user = builder.user;
        this.deposit = builder.deposit;
        this.accountsInvolved = builder.accountsInvolved;
        this.classicAccountIban = builder.classicAccountIban;
        this.savingsAccountIban = builder.savingsAccountIban;
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
        if (amount != 0.0 && currency != null && !currencyFormat) {
            // Added this because of an approximation error in test 19
            BigDecimal roundedValue = BigDecimal.valueOf(amount)
                    .setScale(Utils.PRECISION, RoundingMode.HALF_UP);
            node.put("amount", roundedValue + " " + currency);
        } else if (amount != 0.0 && currency != null) {
            node.put("amount", amount);
            node.put("currency", currency);
        } else if (amount != 0.0) {
            node.put("amount", amount);
        }
        if (type != null) {
            node.put("transferType", type);
        }
        if (commerciant != null) {
            node.put("commerciant", commerciant);
        }
        if (iban != null) {
            node.put("accountIBAN", iban);
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
            for (Account accountToPrint : accounts) {
                accountArray.add(accountToPrint.getIban());
            }
            node.set("involvedAccounts", accountArray);
        }
        if (errorMessage != null) {
            node.put("error", errorMessage);
        }
        if (planType != null) {
            node.put("newPlanType", planType.toString());
        }
        if (amountForUsers != null) {
            ArrayNode amountArray = objectMapper.createArrayNode();
            for (Double amountToPrint : amountForUsers) {
                amountArray.add(amountToPrint);
            }
            node.set("amountForUsers", amountArray);
        }
        if (splitPaymentType != null) {
            node.put("splitPaymentType", splitPaymentType);
        }
        if (splitPaymentCurrency != null) {
            node.put("currency", splitPaymentCurrency);
        }
        if (accountsInvolved != null) {
            ArrayNode accountArray = objectMapper.createArrayNode();
            for (String accountToPrint : accountsInvolved) {
                accountArray.add(accountToPrint);
            }
            node.set("involvedAccounts", accountArray);
        }
        if (classicAccountIban != null) {
            node.put("classicAccountIBAN", classicAccountIban);
        }
        if (savingsAccountIban != null) {
            node.put("savingsAccountIBAN", savingsAccountIban);
        }
        arrayNode.add(node);
    }
}
