package com.museum.util;

public final class Theme {
    public static final String BG_DARK    = "#1c1f2e";
    public static final String BG_PANEL   = "#1e2235";
    public static final String BG_HEADER  = "#1a1d2e";
    public static final String BG_NAVBAR  = "#131623";
    public static final String BG_INPUT   = "#2a2e45";
    public static final String BG_DARKER  = "#161926";

    public static final String BORDER       = "#2a2e45";
    public static final String BORDER_INPUT = "#363b52";

    public static final String TEXT_PRIMARY   = "#e8e8f0";
    public static final String TEXT_SECONDARY = "#a8b0cc";
    public static final String TEXT_MUTED     = "#9095b0";
    public static final String TEXT_DIMMER    = "#5e6480";

    public static final String GOLD   = "#c9a84c";
    public static final String BLUE   = "#5e90ef";
    public static final String GREEN  = "#4caf7d";
    public static final String RED    = "#e05050";
    public static final String PURPLE = "#a78bfa";
    public static final String ORANGE = "#e08030";
    public static final String DARK_PURPLE = "#7c3aed";

    public static String typeColor(String type) {
        return switch (type) {
            case "Artifact"     -> GOLD;
            case "Period"       -> BLUE;
            case "Civilization" -> GREEN;
            case "Person"       -> ORANGE;
            case "Organization" -> "#9b59b6";
            case "Collection"   -> RED;
            default             -> TEXT_MUTED;
        };
    }

    public static String logColor(String changeType) {
        return switch (changeType) {
            case "Created" -> GREEN;
            case "Deleted" -> RED;
            case "Updated" -> BLUE;
            default        -> TEXT_MUTED;
        };
    }
}
