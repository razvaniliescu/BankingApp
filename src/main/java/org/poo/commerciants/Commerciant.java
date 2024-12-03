package org.poo.commerciants;

import org.poo.fileio.CommerciantInput;

import java.util.List;

public class Commerciant {
    private int id;
    private String description;
    private List<String> commerciants;

    public Commerciant(CommerciantInput input) {
        this.id = input.getId();
        this.description = input.getDescription();
        this.commerciants = input.getCommerciants();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCommerciants() {
        return commerciants;
    }

    public void setCommerciants(List<String> commerciants) {
        this.commerciants = commerciants;
    }
}
