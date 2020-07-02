package me.jawdex.reports.commands;

import me.jawdex.reports.helpers.DiscordHelper;
import me.jawdex.reports.object.CommandType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SuggestionCommand implements ICommand {

    private DiscordHelper discordHelper;

    public SuggestionCommand(DiscordHelper discordHelper) {
        this.discordHelper = discordHelper;
    }

    @Override
    public void execute(Player sender, CommandType type, String label, String... args) {
        if (args.length > 0) {
            String message = String.join(" ", args);
            discordHelper.sendSuggestionMessageToDiscord(sender, ChatColor.stripColor(message));
            sender.sendMessage(getMessage(CommandType.SUGGESTION));
        } else {
            sender.sendMessage(ChatColor.RED + "Correct Usage: " + ChatColor.GRAY + "/" + label + " <suggestion>");
        }
    }
}
