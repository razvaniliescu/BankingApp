package org.poo.core;

public class ServicePlans {
    public enum Plans {
        standard,
        student,
        silver,
        gold
    }

    public static int getPlanGrade(Plans plan) {
        return switch (plan) {
            case standard, student -> 0;
            case silver -> 1;
            case gold -> 2;
        };
    }

    public static Plans getPlan(String plan) {
        return switch (plan) {
            case "student" -> ServicePlans.Plans.student;
            case "silver" -> ServicePlans.Plans.silver;
            case "gold" -> ServicePlans.Plans.gold;
            default -> ServicePlans.Plans.standard;
        };
    }
}
