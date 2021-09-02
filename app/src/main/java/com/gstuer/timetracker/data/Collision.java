package com.gstuer.timetracker.data;

import java.util.function.BiPredicate;

public class Collision implements BiPredicate<Entry, Entry> {
    @Override
    public boolean test(Entry entry, Entry otherEntry) {
        return entry.getDay() == otherEntry.getDay() && entry.getStart().isBefore(otherEntry.getEnd());
    }
}
