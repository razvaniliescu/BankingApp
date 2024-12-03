package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public abstract class Command {
    protected String command;
    protected int timestamp;
    public abstract void execute(ObjectMapper objectMapper, ArrayNode arrayNode);
}
