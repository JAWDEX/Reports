package me.jawdex.reports.object;

import org.bukkit.ChatColor;

public enum CommandType {
    BUG("Bug", ChatColor.RED),
    REPORT("Report", ChatColor.BLUE),
    SUGGESTION("Suggestion", ChatColor.AQUA);


    public static CommandType getByName(String name) {
        if (name.equalsIgnoreCase("bug"))
            return BUG;
        if (name.equalsIgnoreCase("report"))
            return REPORT;
        if (name.equalsIgnoreCase("suggestion"))
            return SUGGESTION;
        return null;
    }

    CommandType(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    public String name;
    public ChatColor color;
}
