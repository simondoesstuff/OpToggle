package org.simondoestuff.optoggle;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils {
    public static void sendMsg(CommandSender recipient, String msg) {
        recipient.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}
