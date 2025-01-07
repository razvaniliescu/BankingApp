package org.poo.core.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.core.User;

@Getter @Setter
public class BusinessTransactionInfo {
    private int timestamp;
    private double amount;
    private String type;
    private String commerciant;
    private User user;


}
