package me.jawdex.reports.commands;

import me.jawdex.reports.object.CommandType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface ICommand extends CommandExecutor {

    @Override
    default boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this.");
            return false;
        }

        CommandType type = CommandType.getByName(label);
        if (type == null)
            return false;
        execute((Player) sender, type, label, args);
        return true;
    }

    public void execute(Player sender, CommandType type, String label, String... args);

    default String getMessage(CommandType type) {
        return ChatColor.translateAlternateColorCodes('&', type.color + type.name + " &7» &dYour &7" + type.name.toLowerCase() + " &dhas been sent.");
    }
}
