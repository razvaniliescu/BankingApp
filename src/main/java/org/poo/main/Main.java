package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.checker.Checker;
import org.poo.checker.CheckerConstants;
import org.poo.commands.Command;
import org.poo.commands.CommandFactory;
import org.poo.commerciants.Commerciant;
import org.poo.core.User;
import org.poo.core.exchange.ExchangeGraph;
import org.poo.core.exchange.ExchangeRate;
import org.poo.fileio.*;
import org.poo.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        var sortedFiles = Arrays.stream(Objects.requireNonNull(directory.listFiles())).
                sorted(Comparator.comparingInt(Main::fileConsumer))
                .toList();

        for (File file : sortedFiles) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(CheckerConstants.TESTS_PATH + filePath1);
        ObjectInput inputData = objectMapper.readValue(file, ObjectInput.class);

        ArrayNode output = objectMapper.createArrayNode();

        // Reset the random seed
        Utils.resetRandom();

        // Get the users from the input
        UserInput[] userInput = inputData.getUsers();
        ArrayList<User> userList = new ArrayList<>();
        for (UserInput input : userInput) {
            User newUser = new User(input);
            userList.add(newUser);
        }

        // Get the commerciants from the input
        CommerciantInput[] commerciantInput = inputData.getCommerciants();
        ArrayList<Commerciant> commerciantList = new ArrayList<>();
        for (CommerciantInput input : commerciantInput) {
            Commerciant newCommerciant = new Commerciant(input);
            commerciantList.add(newCommerciant);
        }

        // Get the exchange rates from the input and create the graph
        ExchangeInput[] exchangeRateInput = inputData.getExchangeRates();
        ArrayList<ExchangeRate> exchangeRateList = new ArrayList<>();
        for (ExchangeInput input : exchangeRateInput) {
            ExchangeRate exchangeRate = new ExchangeRate(input);
            exchangeRateList.add(exchangeRate);
        }
        ExchangeGraph exchangeRates = new ExchangeGraph(exchangeRateList);

        // Get the commands from the input...
        CommandInput[] commands = inputData.getCommands();
        ArrayList<Command> commandList = new ArrayList<>();
        for (CommandInput command : commands) {
            Command newCommand = CommandFactory.createCommand(command);
            commandList.add(newCommand);
        }

        // ...and execute them
        for (Command command : commandList) {
            if (command != null) {
                command.execute(objectMapper, output, userList, exchangeRates);
            }
        }

        // Write the output in the output
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

    /**
     * Method used for extracting the test number from the file name.
     *
     * @param file the input file
     * @return the extracted numbers
     */
    public static int fileConsumer(final File file) {
        return Integer.parseInt(
                file.getName()
                        .replaceAll(CheckerConstants.DIGIT_REGEX, CheckerConstants.EMPTY_STR)
        );
    }
}