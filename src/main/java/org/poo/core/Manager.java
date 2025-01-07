package org.poo.core;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Manager extends User {
    private double spent;
    private double deposited;

    public Manager(User user) {
        super(user);
    }
}
