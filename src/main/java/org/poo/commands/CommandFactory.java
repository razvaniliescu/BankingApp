package org.poo.commands;

import org.poo.commands.commandTypes.*;
import org.poo.commands.commandTypes.debug.PrintTransactions;
import org.poo.commands.commandTypes.debug.PrintUsers;
import org.poo.commands.commandTypes.payments.PayOnline;
import org.poo.commands.commandTypes.payments.SendMoney;
import org.poo.commands.commandTypes.payments.SplitPayment;
import org.poo.commands.commandTypes.reports.Report;
import org.poo.commands.commandTypes.reports.SpendingsReport;
import org.poo.fileio.CommandInput;

public class CommandFactory {
    public static Command createCommand(CommandInput input) {
        switch (input.getCommand()) {
            case "printUsers": return new PrintUsers(input);
            case "printTransactions": return new PrintTransactions(input);
            case "addAccount": return new AddAccount(input);
            case "addFunds":  return new AddFunds(input);
            case "createCard":  return new CreateCard(input);
            case "createOneTimeCard":  return new CreateOneTimeCard(input);
            case "deleteAccount": return new DeleteAccount(input);
            case "deleteCard":  return new DeleteCard(input);
            case "setMinBalance":  return new SetMinBalance(input);
            case "checkCardStatus": return new CheckCardStatus(input);
            case "payOnline":  return new PayOnline(input);
            case "sendMoney": return new SendMoney(input);
            case "splitPayment": return new SplitPayment(input);
            case "setAlias":  return new SetAlias(input);
            case "addInterest": return new AddInterest(input);
            case "changeInterestRate": return new ChangeInterestRate(input);
            case "report":  return new Report(input);
            case "spendingsReport": return new SpendingsReport(input);
            default: return null;
        }
    }
}
