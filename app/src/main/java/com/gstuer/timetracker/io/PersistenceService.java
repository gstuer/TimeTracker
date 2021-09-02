package com.gstuer.timetracker.io;

import com.gstuer.timetracker.data.MonthOfWork;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;

public class PersistenceService {
    private static final String PATH_PREFIX = "/TimeTracker/Test/Abcdedae/fasfa";
    private static final String FILE_PATTERN = "%1$tY_%1$tB.json";

    public MonthOfWork getMonthOfWork(YearMonth yearMonth) throws PersistenceException {
        // Fetch month file
        Path path = Path.of(PATH_PREFIX, String.format(FILE_PATTERN, yearMonth));
        JsonFile jsonFile = new JsonFile(path);
        MonthOfWork monthOfWork;
        if (jsonFile.exists()) {
            try {
                monthOfWork = jsonFile.read(MonthOfWork.class);
            } catch (IOException exception) {
                throw new PersistenceException("Cannot read file: " + exception.getMessage());
            }
            return monthOfWork;
        }

        // Create a new monthOfWork if there is no persisted one
        return new MonthOfWork(yearMonth.getYear(), yearMonth.getMonthValue(), new ArrayList<>());
    }

    public void persistMonthOfWork(MonthOfWork monthOfWork) throws PersistenceException {
        YearMonth yearMonth = YearMonth.of(monthOfWork.getYear(), monthOfWork.getMonth());
        Path path = Path.of(PATH_PREFIX, String.format(FILE_PATTERN, yearMonth));
        JsonFile jsonFile = new JsonFile(path);
        try {
            jsonFile.write(monthOfWork);
        } catch (IOException exception) {
            throw new PersistenceException("Cannot persist file: " + exception.getMessage());
        }
    }
}
