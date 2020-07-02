package me.jawdex.reports;

import lombok.Getter;
import me.jawdex.reports.commands.BugCommand;
import me.jawdex.reports.commands.SuggestionCommand;
import me.jawdex.reports.helpers.ChatHelper;
import me.jawdex.reports.helpers.SqlHelper;
import me.jawdex.reports.commands.ReportCommand;
import me.jawdex.reports.helpers.DiscordHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Reports extends JavaPlugin {
    @Getter private DiscordHelper discordHelper;
    @Getter private SqlHelper sqlHelper;
    @Getter private ChatHelper chatHelper;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        this.sqlHelper = new SqlHelper(config.getString("sql.ip"), config.getString("sql.port"), config.getString("sql.database"),
                config.getString("sql.username"), config.getString("sql.password"));
        this.sqlHelper.connect();
        this.chatHelper = new ChatHelper(this, getConfig().getStringList("servers"), getConfig().getString("server-name"));
        this.discordHelper = new DiscordHelper(sqlHelper, chatHelper);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", chatHelper);
        getCommand("report").setExecutor(new ReportCommand(discordHelper));
        getCommand("bug").setExecutor(new BugCommand(discordHelper));
        getCommand("suggestion").setExecutor(new SuggestionCommand(discordHelper));
    }

    @Override
    public void onDisable() {

    }
}
