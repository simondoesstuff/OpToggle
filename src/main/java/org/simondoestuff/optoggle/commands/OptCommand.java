package org.simondoestuff.optoggle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.UUID;

import static org.simondoestuff.optoggle.OpToggle.PREFIX;
import static org.simondoestuff.optoggle.Utils.sendMsg;

public class OptCommand implements CommandExecutor {
    private final LinkedList<UUID> list;

    public OptCommand(LinkedList<UUID> list) {
        this.list = list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sendMsg(sender, PREFIX + "&4 You must be a super-oped player to use this command.");
            return true;
        }

        if (args != null && args.length != 0) {
            sendMsg(sender, PREFIX + "&4 You do not need to provide any arguments.");
        }

        Player user = (Player) sender;

        synchronized (list) {
            if (!list.contains(user.getUniqueId())) {
                sendMsg(sender, PREFIX + "&4 You cannot use this command because you are not super-oped.");
                return true;
            }
        }

        user.setOp(!user.isOp());
        sendMsg(sender, PREFIX + " You were " + (user.isOp() ? "&aoped." : "&4deoped."));

        return true;
    }
}
