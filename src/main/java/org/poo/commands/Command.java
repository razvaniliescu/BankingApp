package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public abstract class Command {
    protected String command;
    protected int timestamp;
    public abstract void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<Transaction> transactions);

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
