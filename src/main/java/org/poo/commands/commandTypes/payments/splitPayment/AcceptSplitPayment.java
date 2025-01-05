package org.poo.commands.commandTypes.payments.splitPayment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.commands.Command;
import org.poo.commerciants.Commerciant;
import org.poo.core.User;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class AcceptSplitPayment extends Command {
    private String email;

    public AcceptSplitPayment(CommandInput input) {
        super(input);
        email = input.getEmail();
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        for (User user : users) {
            if (email.equals(user.getEmail())) {
                SplitPayment pendingPayment = user.getPendingPayments().getFirst();
                pendingPayment.setUsersToAccept(pendingPayment.getUsersToAccept() - 1);
                if (pendingPayment.getUsersToAccept() == 0) {
                    pendingPayment.processPayment(objectMapper, output, users, rates, commerciants);
                }
                user.getPendingPayments().remove(pendingPayment);
            }
        }
    }
}
