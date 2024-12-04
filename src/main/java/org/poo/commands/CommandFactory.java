package org.poo.commands;

import org.poo.accounts.Account;
import org.poo.accounts.User;
import org.poo.commands.commandTypes.*;
import org.poo.commands.commandTypes.debug.PrintUsers;
import org.poo.exchange.ExchangeGraph;
import org.poo.exchange.ExchangeRate;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.Objects;

public class CommandFactory {
    public static Command createCommand(CommandInput input, ArrayList<User> users, ExchangeGraph exchangeRates) {
        switch (input.getCommand()) {
            case "printUsers": return new PrintUsers(input, users);
            case "printTransactions": return null;
            case "addAccount": return new AddAccount(input, users);
            case "addFunds":  return new AddFunds(input, users);
            case "createCard":  return new CreateCard(input, users);
            case "createOneTimeCard":  return new CreateOneTimeCard(input, users);
            case "deleteAccount": return new DeleteAccount(input, users);
            case "deleteCard":  return new DeleteCard(input, users);
            case "setMinBalance":  return null;
            case "checkCardStatus": return null;
            case "payOnline":  return new PayOnline(input, users, exchangeRates);
            case "sendMoney": return new SendMoney(input, users, exchangeRates);
            case "setAlias":  return null;
            case "addInterest": return null;
            case "changeInterestRate": return null;
            case "report":  return null;
            case "spendingReport": return null;
            default: return null;
        }
    }
}
