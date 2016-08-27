package com.brohoof.brohoofwarps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class BrohoofWarpsPlugin extends JavaPlugin {
    private Data d;
    private Settings s;
    private CommandExecutor ch;

    @Override
    public void onEnable() {
        s = new Settings(this);
        d = new Data(this, s);
        ch = new CommandExecutor(this, d, s);
    }

    @Override
    public void onDisable() {
        d.closeConnection();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        return ch.execute(sender, args);
    }
}
