package org.poo.commands;

import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.commandTypes.AddAccount;
import org.poo.commands.commandTypes.AddFunds;
import org.poo.commands.commandTypes.CreateCard;
import org.poo.commands.commandTypes.DeleteAccount;
import org.poo.commands.commandTypes.debug.PrintUsers;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.Objects;

public class CommandFactory {
    public static Command createCommand(CommandInput input, ArrayList<User> users) {
        switch (input.getCommand()) {
            case "printUsers": return new PrintUsers(input, users);
            case "printTransactions": return null;
            case "addAccount": return new AddAccount(input, users);
            case "addFunds":  return new AddFunds(input, users);
            case "createCard":  return new CreateCard(input, users);
            case "createOneTimeCard":  return null;
            case "deleteAccount": return new DeleteAccount(input, users);
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
