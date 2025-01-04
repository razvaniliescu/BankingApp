package org.poo.commerciants;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommerciantInput;

@Getter @Setter
public class Commerciant {
    private String commerciant;
    private int id;
    private String account;
    private String type;
    private String cashbackStrategy;

    public Commerciant(CommerciantInput commerciant) {
        this.commerciant = commerciant.getCommerciant();
        this.id = commerciant.getId();
        this.account = commerciant.getAccount();
        this.type = commerciant.getType();
        this.cashbackStrategy = commerciant.getCashbackStrategy();
    }
}
