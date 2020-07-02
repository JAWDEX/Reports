package me.jawdex.reports.helpers;

import me.jawdex.reports.object.CommandType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DiscordHelper {
    private SqlHelper sqlHelper;
    private ChatHelper chatHelper;

    public DiscordHelper(SqlHelper sqlHelper, ChatHelper chatHelper) {
        this.sqlHelper = sqlHelper;
        this.chatHelper = chatHelper;
    }

    public void sendReportMessageToDiscord(Player sender, Player target, String reason) {
        sendMessage(CommandType.REPORT, sender, target, reason);
        chatHelper.sendToLocal(ChatColor.translateAlternateColorCodes('&', "&9Report" + " &7» &9" + target.getName() + " &7has been reported by &9" + sender.getName() + " &7for &9" + reason));
    }

    public void sendBugMessageToDiscord(Player sender, String reason) {
        sendMessage(CommandType.BUG, sender, null, reason);
    }

    public void sendSuggestionMessageToDiscord(Player sender, String reason) {
        sendMessage(CommandType.SUGGESTION, sender, null, reason);
    }

    private void sendMessage(CommandType type, Player sender, Player target, String reason) {
        try {
            PreparedStatement statement = sqlHelper.getConnection().prepareStatement("INSERT INTO `DiscordMessages` VALUES(?, ?, ?, ?)");
            statement.setString(1, type.name);
            statement.setString(2, sender.getName());
            statement.setString(3, target == null ? "NONE" : target.getName());
            statement.setString(4, reason);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
