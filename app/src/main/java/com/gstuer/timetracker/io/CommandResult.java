package com.gstuer.timetracker.io;

import java.util.Optional;

public class CommandResult {
    private final boolean isSuccess;
    private final Optional<String> optionalMessage;

    public CommandResult(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.optionalMessage = Optional.of(message);
    }

    public CommandResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.optionalMessage = Optional.empty();
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Optional<String> getOptionalMessage() {
        return optionalMessage;
    }
}
