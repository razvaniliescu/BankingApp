package org.poo.commands;

import org.poo.commands.commandTypes.account.*;
import org.poo.commands.commandTypes.card.CheckCardStatus;
import org.poo.commands.commandTypes.card.CreateCard;
import org.poo.commands.commandTypes.card.CreateOneTimeCard;
import org.poo.commands.commandTypes.card.DeleteCard;
import org.poo.commands.commandTypes.debug.PrintTransactions;
import org.poo.commands.commandTypes.debug.PrintUsers;
import org.poo.commands.commandTypes.payments.*;
import org.poo.commands.commandTypes.reports.Report;
import org.poo.commands.commandTypes.reports.SpendingsReport;
import org.poo.fileio.CommandInput;

public final class CommandFactory {
    private CommandFactory() {

    }

    /**
     * Creates commands based on the name given in the input
     * @return a specific command object
     */
    public static Command createCommand(final CommandInput input) {
        return switch (input.getCommand()) {
            case "printUsers" -> new PrintUsers(input);
            case "printTransactions" -> new PrintTransactions(input);
            case "addAccount" -> new AddAccount(input);
            case "addFunds" -> new AddFunds(input);
            case "createCard" -> new CreateCard(input);
            case "createOneTimeCard" -> new CreateOneTimeCard(input);
            case "deleteAccount" -> new DeleteAccount(input);
            case "deleteCard" -> new DeleteCard(input);
            case "setMinBalance" -> new SetMinBalance(input);
            case "checkCardStatus" -> new CheckCardStatus(input);
            case "payOnline" -> new PayOnline(input);
            case "sendMoney" -> new SendMoney(input);
            case "splitPayment" -> new SplitPayment(input);
            case "setAlias" -> new SetAlias(input);
            case "addInterest" -> new AddInterest(input);
            case "changeInterestRate" -> new ChangeInterestRate(input);
            case "report" -> new Report(input);
            case "spendingsReport" -> new SpendingsReport(input);
            default -> null;
        };
    }
}
