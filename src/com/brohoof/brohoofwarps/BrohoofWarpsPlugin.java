package com.brohoof.brohoofwarps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class.
 * 
 */
// We start at {@link #onEnable()}, and commands are through {@link #onCommand(CommandSender, Command, String, String[])}
public class BrohoofWarpsPlugin extends JavaPlugin {
    /**
     * Static field to keep a copy of this plugin.
     */
    private static BrohoofWarpsPlugin i;
    private Settings s;

    @Override
    public void onEnable() {
        // 'this' refers to this object. In this case, it's an instance of BrohoofWarpsPlugin
        i = this;
        s = new Settings(this);
    }

    @Override
    public void onDisable() {
        Data.closeConnection();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        // Delegate our command execution to the CommandExecutor
        return CommandExecutor.onCommand(sender, args);
    }

    /**
     * Gets an instance of the plugin
     * 
     * @return a copy of the plugin
     */
    public static BrohoofWarpsPlugin getPlugin() {
        return i;
    }

    public Settings getSettings() {
        return s;
    }
}
