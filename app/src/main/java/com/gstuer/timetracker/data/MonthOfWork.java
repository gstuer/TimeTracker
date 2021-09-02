package com.gstuer.timetracker.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MonthOfWork {
    private static final String JSON_ALIAS_YEAR = "year";
    private static final String JSON_ALIAS_MONTH = "month";
    private static final String JSON_ALIAS_ENTRIES = "entries";

    @JsonProperty(JSON_ALIAS_YEAR)
    private final int year;
    @JsonProperty(JSON_ALIAS_MONTH)
    private final int month;
    @JsonProperty(JSON_ALIAS_ENTRIES)
    private final List<Entry> entries;

    @JsonCreator
    public MonthOfWork(@JsonProperty(JSON_ALIAS_YEAR) int year,
                       @JsonProperty(JSON_ALIAS_MONTH) int month,
                       @JsonProperty(JSON_ALIAS_ENTRIES) List<Entry> entries) {
        this.year = year;
        this.month = month;
        this.entries = entries;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public void addEntry(Entry entry) {
        if (existsCollision(entry)) {
            throw new IllegalArgumentException("Cannot add entry due to collision.");
        }
        this.entries.add(entry);
    }

    public void removeEntry(Entry entry) {
        this.entries.remove(entry);
    }

    public boolean existsCollision(Entry entry) {
        return this.getEntries().stream().anyMatch(other -> new Collision().test(entry, other));
    }
}
