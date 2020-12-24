package org.simondoestuff.optoggle;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.simondoestuff.optoggle.commands.OptCommand;
import org.simondoestuff.optoggle.commands.SuperOPCommand;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.simondoestuff.optoggle.Utils.filterNullsOnUUIDList;
import static org.simondoestuff.optoggle.Utils.sendMsg;

public final class OpToggle extends JavaPlugin {
    private static OpToggle instance = null;
    public static final String PREFIX = "&7[&f&nOPT&7]&3";
    private final LinkedList<UUID> list = new LinkedList<>();

    public OpToggle() {
        instance = this;
    }

    public static OpToggle getInst() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        List<String> oplist = getConfig().getStringList("oplist");
        oplist.forEach(s -> list.add(UUID.fromString(s)));
        filterNullsOnUUIDList(list);

        Bukkit.getPluginCommand("OPToggle").setExecutor(new OptCommand(list));
        Bukkit.getPluginCommand("SuperOP").setExecutor(new SuperOPCommand(list));

        sendMsg(Bukkit.getServer().getConsoleSender(), PREFIX + " &aEnabled successfully!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        synchronized (list) {
            ArrayList<String> writeList = new ArrayList<>();
            list.forEach(uuid -> writeList.add(uuid.toString()));
            filterNullsOnUUIDList(list);

            getConfig().set("oplist", writeList);
        }

        saveConfig();
        sendMsg(Bukkit.getServer().getConsoleSender(), PREFIX + " &aDisabled successfully!");
    }
}
