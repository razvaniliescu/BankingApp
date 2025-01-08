package org.poo.commands.commandTypes.payments.splitPayment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.commands.Command;
import org.poo.commerciants.Commerciant;
import org.poo.core.User;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.exceptions.MyException;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class AcceptSplitPayment extends Command {
    private String email;
    private String type;

    public AcceptSplitPayment(CommandInput input) {
        super(input);
        email = input.getEmail();
        type = input.getSplitPaymentType();
    }

    @Override
    public void execute(ObjectMapper objectMapper, ArrayNode output, ArrayList<User> users, ExchangeGraph rates, ArrayList<Commerciant> commerciants) {
        try {
            for (User user : users) {
                if (email.equals(user.getEmail())) {
                    if (!user.getPendingPayments().isEmpty()) {
                        for (int i = 0; i < user.getPendingPayments().size(); i++) {
                            if (user.getPendingPayments().get(i).getType().equals(type)) {
                                SplitPayment pendingPayment = user.getPendingPayments().get(i);
                                pendingPayment.getUsersInvolved().add(user);
                                pendingPayment.setUsersToAccept(pendingPayment.getUsersToAccept() - 1);
                                if (pendingPayment.getUsersToAccept() == 0) {
                                    pendingPayment.processPayment(objectMapper, output, users, rates, commerciants);
                                }
                                user.getPendingPayments().remove(pendingPayment);
                                return;
                            }
                        }
                        return;
                    }
                }
            } throw new MyException("User not found");
        } catch (MyException e) {
            e.printException(objectMapper, output, command, timestamp);
        }
    }
}
