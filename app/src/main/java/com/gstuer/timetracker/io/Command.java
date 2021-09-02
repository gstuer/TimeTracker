package com.gstuer.timetracker.io;

import com.gstuer.timetracker.data.Entry;
import com.gstuer.timetracker.data.MonthOfWork;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Optional;

public enum Command {
    START("start") {
        private final static int INDEX_DESCRIPTION = 0;

        @Override
        public CommandResult execute(String[] parameters) {
            // Check if enough parameters exist
            if (parameters.length < 1) {
                return new CommandResult(false, "Use: start <description>");
            }

            // Get current time
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Get current month file
            PersistenceService persistenceService = new PersistenceService();
            MonthOfWork monthOfWork;
            try {
                monthOfWork = persistenceService.getMonthOfWork(YearMonth.from(currentDateTime));
            } catch (PersistenceException exception) {
                return new CommandResult(false, exception.getMessage());
            }

            // Get parameters and create new entry
            String description = parameters[INDEX_DESCRIPTION];
            // TODO Add rounding options for start time
            Entry entry = new Entry(description, currentDateTime.getDayOfMonth(), currentDateTime.toLocalTime(),
                    LocalTime.MAX, false);

            // Check if a collision exists with a started entry
            if (monthOfWork.existsCollision(entry)) {
                return new CommandResult(false, "Tracker already started.");
            }

            // Persist month of work
            monthOfWork.addEntry(entry);
            try {
                persistenceService.persistMonthOfWork(monthOfWork);
            } catch (PersistenceException exception) {
                return new CommandResult(false, exception.getMessage());
            }
            return new CommandResult(true, "Tracker started.");
        }
    },
    STOP("stop") {
        @Override
        public CommandResult execute(String[] parameters) {
            // Get current time
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Get current month file
            PersistenceService persistenceService = new PersistenceService();
            MonthOfWork monthOfWork;
            try {
                monthOfWork = persistenceService.getMonthOfWork(YearMonth.from(currentDateTime));
            } catch (PersistenceException exception) {
                return new CommandResult(false, exception.getMessage());
            }

            // Get currently tracked (=unfinished) entry
            Optional<Entry> optionalTrackedEntry = monthOfWork.getEntries().stream()
                    .filter(other -> other.getEnd().isAfter(currentDateTime.toLocalTime()))
                    .findFirst();
            if (optionalTrackedEntry.isEmpty()) {
                return new CommandResult(false, "No unfinished entry found.");
            }

            // Remove tracked and add untracked entry to month of work
            Entry entry = optionalTrackedEntry.get();
            monthOfWork.removeEntry(entry);
            // TODO Add rounding options for end time
            entry.setEnd(currentDateTime.toLocalTime());
            monthOfWork.addEntry(entry);

            // Persist month of work
            try {
                persistenceService.persistMonthOfWork(monthOfWork);
            } catch (PersistenceException exception) {
                return new CommandResult(false, exception.getMessage());
            }
            return new CommandResult(true, "Tracker stopped.");
        }
    },
    PAUSE("pause") {
        @Override
        public CommandResult execute(String[] parameters) {
            // TODO Implement command
            return new CommandResult(false, "Not implemented yet.");
        }
    };

    private final String regex;

    Command(String regex) {
        this.regex = regex;
    }

    public abstract CommandResult execute(String[] parameters);

    public static Optional<Command> findMatchingCommand(String stringRepresentation) {
        return Arrays.stream(Command.values())
                .filter(command -> command.regex.matches(stringRepresentation))
                .findFirst();
    }
}
