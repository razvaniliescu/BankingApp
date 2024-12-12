package org.poo.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Exception class with added print method
 */
public class MyException extends Exception {
    public MyException(final String message) {
        super(message);
    }

    /**
     * Prints the exception in JSON format
     */
    public void printException(final ObjectMapper objectMapper, final ArrayNode output,
                               final String command, final int timestamp) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", command);
        ObjectNode result = objectMapper.createObjectNode();
        result.put("description", this.getMessage());
        result.put("timestamp", timestamp);
        node.set("output", result);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
