package me.jawdex.reports.commands;

import me.jawdex.reports.helpers.DiscordHelper;
import me.jawdex.reports.object.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReportCommand implements ICommand {
    private DiscordHelper discordHelper;

    public ReportCommand(DiscordHelper discordHelper) {
        this.discordHelper = discordHelper;
    }

    @Override
    public void execute(Player sender, CommandType type, String label, String... args) {
        if (args.length > 1) {
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "That player is not online.");
                return;
            }
            discordHelper.sendReportMessageToDiscord(sender, target, ChatColor.stripColor(message));
            sender.sendMessage(getMessage(CommandType.REPORT));
        } else {
            sender.sendMessage(ChatColor.RED + "Correct Usage: " + ChatColor.GRAY + "/" + label + " <player> <reason>");
        }
    }
}
