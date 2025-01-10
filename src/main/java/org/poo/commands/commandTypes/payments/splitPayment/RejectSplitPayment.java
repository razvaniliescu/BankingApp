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

public class RejectSplitPayment extends Command {
    private String email;
    private String type;

    public RejectSplitPayment(CommandInput input) {
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
                                user.getPendingPayments().get(i).rejectPayment(users);
                                return;
                            }
                        }
                    }
                    return;
                }
            } throw new MyException("User not found");
        } catch (MyException e) {
            e.printException(objectMapper, output, command, timestamp);
        }

    }
}
