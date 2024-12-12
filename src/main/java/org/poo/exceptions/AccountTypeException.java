package org.poo.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AccountTypeException extends MyException {
    public AccountTypeException() {
        super("This kind of report is not supported for a saving account");
    }

    /**
     * Modified version of the print because this exception
     * requires a different format
     */
    public void printException(final ObjectMapper objectMapper, final ArrayNode output,
                               final String command, final int timestamp) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("command", command);
        ObjectNode result = objectMapper.createObjectNode();
        result.put("error", this.getMessage());
        node.set("output", result);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
