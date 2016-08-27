package com.brohoof.brohoofwarps;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * The class that holds our settings 
 *
 */
public class Settings {
    /**
     * The name of the database
     */
    public String dbDatabase;
    /**
     * The address of the database
     */
    public String dbHost;
    /**
     * The password to the database
     */
    public String dbPass;
    /**
     * The port number to the database
     */
    public int dbPort;
    /**
     * The prefix to use for the database.
     */
    public String dbPrefix;
    /**
     * The username to the database
     */
    public String dbUser;
    /**
     * A copy of the {@link BrohoofWarpsPlugin}
     */
    private final BrohoofWarpsPlugin plugin;
    /**
     * Should we print stack traces?
     */
    public boolean stackTraces;

    /**
     * Creaates a new Settings
     * @param p the plugin
     */
    public Settings(BrohoofWarpsPlugin p) {
        plugin = p;
        readSettings(plugin.getConfig());
    }

    /**
     * Reads settings
     *
     * @param pConfig the {@link FileConfiguration} to use
     */
    private void readSettings(final FileConfiguration config) {
        stackTraces = config.getBoolean("general.printStackTraces");
        dbHost = config.getString("database.host");
        dbPort = config.getInt("database.port");
        dbUser = config.getString("database.username");
        dbPass = config.getString("database.password");
        dbDatabase = config.getString("database.database");
        dbPrefix = config.getString("database.prefix");
    }

    /**
     * Reloads settings
     */
    public void reloadSettings() {
        plugin.reloadConfig();
        readSettings(plugin.getConfig());
    }
}
