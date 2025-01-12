package org.poo.core.user;


import org.poo.utils.Utils;

/**
 * Class for processing service plans
 */
public final class ServicePlans {
    public enum Plans {
        standard,
        student,
        silver,
        gold
    }

    private ServicePlans() {

    }

    /**
     * Converts the plan to a numeric value.
     * Used to calculate the price for an upgrade
     */
    public static int getPlanGrade(final Plans plan) {
        return switch (plan) {
            case standard, student -> Utils.STANDARD_PLAN_VALUE;
            case silver -> Utils.SILVER_PLAN_VALUE;
            case gold -> Utils.GOLD_PLAN_VALUE;
        };
    }

    /**
     * Used to get the plan for the input
     */
    public static Plans getPlan(final String plan) {
        return switch (plan) {
            case "student" -> ServicePlans.Plans.student;
            case "silver" -> ServicePlans.Plans.silver;
            case "gold" -> ServicePlans.Plans.gold;
            default -> ServicePlans.Plans.standard;
        };
    }
}
