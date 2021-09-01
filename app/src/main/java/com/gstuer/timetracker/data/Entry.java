package com.gstuer.timetracker.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entry {
    private static final String JSON_PATTERN_TIME = "HH:mm";
    private static final String JSON_ALIAS_DESCRIPTION = "action";
    private static final String JSON_ALIAS_DAY = "day";
    private static final String JSON_ALIAS_START = "start";
    private static final String JSON_ALIAS_END = "end";
    private static final String JSON_ALIAS_VACATION = "vacation";

    @JsonProperty(JSON_ALIAS_DESCRIPTION) private final String description;
    @JsonProperty(JSON_ALIAS_DAY) private final int day;
    @JsonProperty(JSON_ALIAS_START) @JsonFormat(pattern = JSON_PATTERN_TIME) private final LocalTime start;
    @JsonProperty(JSON_ALIAS_END) @JsonFormat(pattern = JSON_PATTERN_TIME) private LocalTime end;
    @JsonProperty(JSON_ALIAS_VACATION) private boolean isVacation;

    @JsonCreator
    public Entry(@JsonProperty(JSON_ALIAS_DESCRIPTION) String description,
                 @JsonProperty(JSON_ALIAS_DAY) int day,
                 @JsonProperty(JSON_ALIAS_START) LocalTime start,
                 @JsonProperty(JSON_ALIAS_END) LocalTime end,
                 @JsonProperty(JSON_ALIAS_VACATION) boolean isVacation) {
        this.description = description;
        this.day = day;
        this.start = start;
        this.end = end;
        this.isVacation = isVacation;
    }

    public Entry(String description, int day, LocalTime start, boolean isVacation) {
        this(description, day, start, null, isVacation);
    }

    public String getDescription() {
        return this.description;
    }

    public int getDay() {
        return this.day;
    }


    public LocalTime getStart() {
        return this.start;
    }

    public LocalTime getEnd() {
        return this.end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public boolean isVacation() {
        return this.isVacation;
    }

    public void setVacation(boolean isVacation) {
        this.isVacation = isVacation;
    }
}
