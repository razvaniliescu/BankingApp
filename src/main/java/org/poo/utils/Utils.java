package org.poo.utils;

import java.util.Random;

public final class Utils {
    private Utils() {
        // Checkstyle error free constructor
    }

    private static final int IBAN_SEED = 1;
    private static final int CARD_SEED = 2;
    private static final int DIGIT_BOUND = 10;
    private static final int DIGIT_GENERATION = 16;
    private static final String RO_STR = "RO";
    private static final String POO_STR = "POOB";

    private static Random ibanRandom = new Random(IBAN_SEED);
    private static Random cardRandom = new Random(CARD_SEED);

    public static final int CURRENT_YEAR = 2025;
    public static final int MINIMUM_AGE = 21;

    public static final int STANDARD_PLAN_VALUE = 0;
    public static final int SILVER_PLAN_VALUE = 1;
    public static final int GOLD_PLAN_VALUE = 3;
    public static final double SILVER_TRANSACTION_THRESHOLD = 300;
    public static final int SILVER_TRANSACTIONS = 5;
    public static final double SILVER_COMMISSION_THRESHOLD = 500;
    public static final double SILVER_FEE = 100;
    public static final double GOLD_FEE = 250;

    public static final double STANDARD_COMMISSION = 0.002;
    public static final double SILVER_COMMISSION = 0.001;

    public static final int FOOD_TRANSACTIONS = 2;
    public static final int CLOTHES_TRANSACTIONS = 5;
    public static final int TECH_TRANSACTIONS = 10;

    public static final double FOOD_CASHBACK = 0.02;
    public static final double CLOTHES_CASHBACK = 0.05;
    public static final double TECH_CASHBACK = 0.1;

    public static final double SMALL_SPENDING_THRESHOLD = 100;
    public static final double MEDIUM_SPENDING_THRESHOLD = 300;
    public static final double LARGE_SPENDING_THRESHOLD = 500;

    public static final double SMALL_STANDARD_CASHBACK = 0.001;
    public static final double MEDIUM_STANDARD_CASHBACK = 0.003;
    public static final double LARGE_STANDARD_CASHBACK = 0.005;
    public static final double SMALL_SILVER_CASHBACK = 0.002;
    public static final double MEDIUM_SILVER_CASHBACK = 0.004;
    public static final double LARGE_SILVER_CASHBACK = 0.0055;
    public static final double SMALL_GOLD_CASHBACK = 0.0025;
    public static final double MEDIUM_GOLD_CASHBACK = 0.005;
    public static final double LARGE_GOLD_CASHBACK = 0.007;

    public static final int PRECISION = 3;

    public static final double INITIAL_LIMIT = 500;
    /**
     * Utility method for generating an IBAN code.
     *
     * @return the IBAN as String
     */
    public static String generateIBAN() {
        StringBuilder sb = new StringBuilder(RO_STR);
        for (int i = 0; i < RO_STR.length(); i++) {
            sb.append(ibanRandom.nextInt(DIGIT_BOUND));
        }

        sb.append(POO_STR);
        for (int i = 0; i < DIGIT_GENERATION; i++) {
            sb.append(ibanRandom.nextInt(DIGIT_BOUND));
        }

        return sb.toString();
    }

    /**
     * Utility method for generating a card number.
     *
     * @return the card number as String
     */
    public static String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DIGIT_GENERATION; i++) {
            sb.append(cardRandom.nextInt(DIGIT_BOUND));
        }

        return sb.toString();
    }

    /**
     * Resets the seeds between runs.
     */
    public static void resetRandom() {
        ibanRandom = new Random(IBAN_SEED);
        cardRandom = new Random(CARD_SEED);
    }
}
