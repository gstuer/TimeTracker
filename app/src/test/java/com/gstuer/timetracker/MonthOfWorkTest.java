package com.gstuer.timetracker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gstuer.timetracker.data.Entry;
import com.gstuer.timetracker.data.MonthOfWork;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MonthOfWorkTest {
    @Test
    public void testMapToJson() throws JsonProcessingException {
        // Preparation Entry
        String description = "TestAction";
        int day = 31;
        LocalTime start = LocalTime.of(12, 30, 0);
        LocalTime end = start.plusMinutes(35);
        boolean isVacation = false;
        Entry entry = new Entry(description, day, start, end, isVacation);

        // Preparation MonthOfWork
        int year = 2021;
        int month = 8;
        MonthOfWork monthOfWork = new MonthOfWork(year, month, List.of(entry));

        // Execution
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        String mapperOutput = mapper.writeValueAsString(monthOfWork);
        System.out.println(mapperOutput);

        // Assertions
        assertTrue(mapperOutput.contains("\"year\":" + year + ""));
        assertTrue(mapperOutput.contains("\"month\":" + month));
        assertTrue(mapperOutput.contains("\"entries\":["));
    }

    @Test
    public void testCreateFromJson() throws JsonProcessingException {
        // Preparation Entry
        String description = "TestAction";
        int day = 31;
        LocalTime start = LocalTime.of(12, 30, 0);
        boolean isVacation = false;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String entryPattern = "{\"action\":\"%s\",\"day\":%d,\"start\":\"%s\",\"vacation\":%s}";
        String entryString = String.format(entryPattern, description, day, start.format(dateTimeFormatter), isVacation);

        // Preparation MonthOfWork
        int year = 2021;
        int month = 8;
        String monthOfWorkPattern = "{\"year\":%d,\"month\":%d,\"entries\":[%s]}";
        String monthOfWorkString = String.format(monthOfWorkPattern, year, month, entryString);

        // Execution
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        MonthOfWork monthOfWork = mapper.readValue(monthOfWorkString, MonthOfWork.class);

        // Assertions
        assertNotNull(monthOfWork);
        assertEquals(year, monthOfWork.getYear());
        assertEquals(month, monthOfWork.getMonth());
        assertFalse(monthOfWork.getEntries().isEmpty());
    }
}
