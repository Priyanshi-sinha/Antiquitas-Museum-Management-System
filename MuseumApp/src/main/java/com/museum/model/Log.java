package com.museum.model;

import java.util.ArrayList;
import java.util.List;

public class Log {
    public String id, itemType, itemId, itemName, changedBy, changeType, changedAt;
    public List<LogChange> changes = new ArrayList<>();

    public static class LogChange {
        public String field, oldValue, newValue;
        public LogChange() {}
        public LogChange(String f, String o, String n) { field = f; oldValue = o; newValue = n; }
    }
}
