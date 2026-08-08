package com.library.enums;

public enum Genre {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    SCIENCE("Science"),
    TECHNOLOGY("Technology"),
    HISTORY("History"),
    BIOGRAPHY("Biography");

    private final String displayName;

    Genre(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}