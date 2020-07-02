package me.jawdex.reports.helpers;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.jawdex.reports.Reports;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.List;

public class ChatHelper implements PluginMessageListener {
    private Reports plugin;
    private List<String> localServers;
    private String serverName;

    public ChatHelper(Reports plugin, List<String> localServers, String serverName) {
        this.plugin = plugin;
        this.localServers = localServers;
        this.serverName = serverName;
    }
    public void sendToLocal(String message) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p.hasPermission("reports.receive")) {
                p.sendMessage(message);
            }
        });
        localServers.forEach(s -> {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF(s);
            out.writeUTF("Reports");
            sendMessage(message, out);
        });
    }

    private void sendMessage(String message, ByteArrayDataOutput out) {
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF(ChatColor.GRAY + "[" + serverName + "] " +  message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());
        if (player != null) {
            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equalsIgnoreCase("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subChannel = in.readUTF();
            if (subChannel.equalsIgnoreCase("Reports")) {
                short len = in.readShort();
                byte[] msgbytes = new byte[len];
                in.readFully(msgbytes);

                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
                try {
                    String msg = msgin.readUTF();
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        if (p.hasPermission("reports.receive")) {
                            p.sendMessage(msg);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
