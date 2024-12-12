package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.core.User;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

@Setter
@Getter
public abstract class Command {
    protected String command;
    protected int timestamp;

    public Command(final CommandInput input) {
        this.command = input.getCommand();
        this.timestamp = input.getTimestamp();
    }

    /**
     * Executes the command and adds its result to the output
     * @param objectMapper used to create JSON objects
     * @param output the array node that will be written in the output file
     * @param users the list of all users
     * @param rates the exchange rate graph
     */
    public abstract void execute(ObjectMapper objectMapper, ArrayNode output,
                                 ArrayList<User> users, ExchangeGraph rates);
}
