package com.gstuer.timetracker;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gstuer.timetracker.data.Entry;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EntryTest {
    @Test
    public void testMapToJson() throws JsonProcessingException {
        // Preparation
        String description = "TestAction";
        int day = 31;
        LocalTime start = LocalTime.of(12, 30, 0);
        LocalTime end = start.plusMinutes(35);
        boolean isVacation = false;
        Entry entry = new Entry(description, day, start, end, isVacation);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Execution
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        String mapperOutput = mapper.writeValueAsString(entry);

        // Assertions
        assertTrue(mapperOutput.contains("\"action\":\"" + description + "\""));
        assertTrue(mapperOutput.contains("\"day\":" + day));
        assertTrue(mapperOutput.contains("\"start\":\"" + start.format(dateTimeFormatter) + "\""));
        assertTrue(mapperOutput.contains("\"end\":\"" + end.format(dateTimeFormatter) + "\""));
        assertTrue(mapperOutput.contains("\"vacation\":" + isVacation));
    }

    @Test
    public void testMapToJsonNullEnd() throws JsonProcessingException {
        // Preparation
        String description = "TestAction";
        int day = 31;
        LocalTime start = LocalTime.of(12, 30, 0);
        boolean isVacation = false;
        Entry entry = new Entry(description, day, start, isVacation);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Execution
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        String mapperOutput = mapper.writeValueAsString(entry);

        // Assertions
        assertTrue(mapperOutput.contains("\"action\":\"" + description + "\""));
        assertTrue(mapperOutput.contains("\"day\":" + day));
        assertTrue(mapperOutput.contains("\"start\":\"" + start.format(dateTimeFormatter) + "\""));
        assertFalse(mapperOutput.contains("\"end\""));
        assertTrue(mapperOutput.contains("\"vacation\":" + isVacation));
    }

    @Test
    public void testCreateFromJson() throws JsonProcessingException {
        // Preparation
        String description = "TestAction";
        int day = 31;
        LocalTime start = LocalTime.of(12, 30, 0);
        LocalTime end = start.plusMinutes(35);
        boolean isVacation = false;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Execution
        String jsonStringPattern = "{\"action\":\"%s\",\"day\":%d,\"start\":\"%s\",\"end\":\"%s\",\"vacation\":%s}";
        String jsonString = String.format(jsonStringPattern, description, day, start.format(dateTimeFormatter),
                end.format(dateTimeFormatter), isVacation);
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        Entry entry = mapper.readValue(jsonString, Entry.class);

        // Assertions
        assertNotNull(entry);
        assertEquals(description, entry.getDescription());
        assertEquals(day, entry.getDay());
        assertEquals(start, entry.getStart());
        assertEquals(end, entry.getEnd());
        assertEquals(isVacation, entry.isVacation());
    }

    @Test
    public void testCreateFromJsonNullEnd() throws JsonProcessingException {
        // Preparation
        String description = "TestAction";
        int day = 31;
        LocalTime start = LocalTime.of(12, 30, 0);
        boolean isVacation = false;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Execution
        String jsonStringPattern = "{\"action\":\"%s\",\"day\":%d,\"start\":\"%s\",\"vacation\":%s}";
        String jsonString = String.format(jsonStringPattern, description, day, start.format(dateTimeFormatter), isVacation);
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        Entry entry = mapper.readValue(jsonString, Entry.class);

        // Assertions
        assertNotNull(entry);
        assertEquals(description, entry.getDescription());
        assertEquals(day, entry.getDay());
        assertEquals(start, entry.getStart());
        assertNull(entry.getEnd());
        assertEquals(isVacation, entry.isVacation());
    }
}
