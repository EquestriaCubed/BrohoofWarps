package com.brohoof.brohoofwarps;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class.
 * 
 */
// We start at {@link #onEnable()}, and commands are through {@link #onCommand(CommandSender, Command, String, String[])}
public class BrohoofWarpsPlugin extends JavaPlugin {
	private Settings s;
	private Data d;
	private CommandExecutor ch;

	@Override
	public void onEnable() {
		s = new Settings(this);
		d = new Data(this, s);
		ch = new CommandExecutor(this, d, s);
		getCommand("warp").setExecutor(ch);
		WarpWorker.setData(d);
		WarpAccess.setPlugin(this);
	}

	@Override
	public void onDisable() {
		d.closeConnection();
	}
}
