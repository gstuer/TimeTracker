package com.gstuer.timetracker;

import com.gstuer.timetracker.io.Command;
import com.gstuer.timetracker.io.CommandResult;

import java.util.Optional;

public final class App {

    private static final int INDEX_COMMAND = 0;
    private static final int INDEX_FIRST_PARAMETER = 1;

    private App() {
        throw new IllegalStateException();
    }

    public static void main(String[] args) {
        // Check if a command was given
        if (args.length < 1) {
            System.out.println("Possible commands: start <description> | end | pause <HH:mm>");
            return;
        }

        // Split command and parameters
        String commandRepresentation = args[INDEX_COMMAND];
        String[] parameters = new String[args.length - 1];
        if (args.length > 1) {
            System.arraycopy(args, INDEX_FIRST_PARAMETER, parameters, 0, parameters.length);
        }

        // Find command
        Optional<Command> optionalCommand = Command.findMatchingCommand(commandRepresentation);
        if (optionalCommand.isEmpty()) {
            System.out.println("No matching command found");
            return;
        }

        // Execute command and print result if necessary
        CommandResult result = optionalCommand.get().execute(parameters);
        result.getOptionalMessage().ifPresent(System.out::println);
    }
}
