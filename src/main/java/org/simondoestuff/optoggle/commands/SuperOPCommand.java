package org.simondoestuff.optoggle.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.simondoestuff.optoggle.OpToggle;

import java.util.LinkedList;
import java.util.UUID;

import static org.simondoestuff.optoggle.OpToggle.PREFIX;
import static org.simondoestuff.optoggle.Utils.sendMsg;

public class SuperOPCommand implements CommandExecutor {
    private final LinkedList<UUID> list;

    public SuperOPCommand(LinkedList<UUID> list) {
        this.list = list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args == null || args.length == 0) list(sender);
        else if (args.length == 1) {
            // RCON uses RemoteConsoleCommandSender. We treat that equivalent to the normal console here.
            if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) toggle(sender, args[0]);
            else sendMsg(sender, PREFIX + "&4 You must be console to use this command.");
        } else sendMsg(sender, PREFIX + "&4 You must provide either 1 or 0 arguments.");

        return true;
    }

    private void toggle(CommandSender sender, String target) {
        sendMsg(sender, PREFIX + " Processing...");

        new BukkitRunnable() {
            @Override
            public void run() {
                OfflinePlayer p = Bukkit.getOfflinePlayer(target);

                if (!p.hasPlayedBefore() && !p.isOnline()) {
                    sendMsg(sender, PREFIX + " &4Player not found. Try again when they are online.");
                    return;
                }

                UUID uuid = p.getUniqueId();
                boolean contains;

                synchronized (list) {
                    contains = list.contains(uuid);

                    if (contains) list.remove(uuid);
                    else list.add(uuid);
                }

                sendMsg(sender, PREFIX + " " + p.getName() + "&3 " + (contains ? "&4is no longer &3super-oped." : "&ais now &3super-oped.&r"));

                Player onlinePlayer = p.getPlayer();

                if (onlinePlayer != null) {
                    sendMsg(onlinePlayer, PREFIX + " You are " + (contains ? "&4no longer &3super-oped." : "&anow &3super-oped.&r"));
                }
            }
        }.runTaskAsynchronously(OpToggle.getInst());
    }

    private void list(CommandSender sender) {
        sendMsg(sender, PREFIX + " Processing...");

        new BukkitRunnable() {
            @Override
            public void run() {
                StringBuilder msg = new StringBuilder("");

                synchronized (list) {
                    if (!(sender instanceof ConsoleCommandSender)) {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;

                            if (!list.contains(p.getUniqueId())) {
                                sendMsg(p, PREFIX + " &4You must be super-oped yourself to list super-ops.");
                                return;
                            }
                        } else {
                            sendMsg(sender, PREFIX + " &4You must be either console or a player to use this command.");
                            return;
                        }
                    }

                    int size = list.size();

                    msg.append(PREFIX + " ").append(size).append(" super-oped player").append((size != 1) ? "s." : ".");

                    for (UUID uuid : list) {
                        msg.append("&r\n&b - ").append(Bukkit.getOfflinePlayer(uuid).getName());
                    }
                }

                msg.append("&r");
                sendMsg(sender, msg.toString());
            }
        }.runTaskAsynchronously(OpToggle.getInst());
    }
}
