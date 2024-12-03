package org.poo.commands;

import org.poo.accounts.User;
import org.poo.commands.commandTypes.AddAccount;
import org.poo.commands.commandTypes.debug.PrintUsers;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class CommandFactory {
    public static Command createCommand(CommandInput input, ArrayList<User> users) {
        User userToExecute = null;
        for (User user : users) {
            if (user.getEmail().equals(input.getEmail())) {
                userToExecute = user;
            }
        }
        switch (input.getCommand()) {
            case "printUsers": return new PrintUsers(input, users);
            case "printTransactions": return null;
            case "addAccount": return new AddAccount(input, userToExecute);
            case "addFunds":  return null;
            case "createCard":  return null;
            case "createOneTimeCard":  return null;
            case "deleteAccount": return null;
            case "deleteCard":  return null;
            case "setMinBalance":  return null;
            case "checkCardStatus": return null;
            case "payOnline":  return null;
            case "sendMoney": return null;
            case "setAlias":  return null;
            case "addInterest": return null;
            case "changeInterestRate": return null;
            case "report":  return null;
            case "spendingReport": return null;
            default: return null;
        }
    }
}
