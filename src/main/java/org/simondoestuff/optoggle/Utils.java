package org.simondoestuff.optoggle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class Utils {
    public static void sendMsg(CommandSender recipient, String msg) {
        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void filterNullsOnUUIDList(LinkedList<UUID> list) {
        ArrayList<UUID> toBeRemoved = new ArrayList<>();

        for (UUID uuid : list) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
            String name = p.getName();

            if (name == null || name.trim().equals("null") || !p.hasPlayedBefore()) {
                toBeRemoved.add(uuid);
            }
        }

        toBeRemoved.forEach(list::remove);
    }
}
