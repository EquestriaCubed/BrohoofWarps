package com.brohoof.brohoofwarps;

import org.bukkit.configuration.file.FileConfiguration;

public class Settings {
    public String dbDatabase;
    public String dbHost;
    public String dbPass;
    public String dbPort;
    public String dbPrefix;
    public String dbUser;
    private final BrohoofWarpsPlugin plugin;
    boolean stackTraces;
    public String suspendReason;

    public Settings(BrohoofWarpsPlugin p) {
        plugin = p;
        readSettings(plugin.getConfig());
    }

    /**
     * Reads settings
     *
     * @param pConfig
     */
    private void readSettings(final FileConfiguration config) {
        stackTraces = config.getBoolean("general.printStackTraces");
        suspendReason = config.getString("general.suspendReason");
        dbHost = config.getString("database.host");
        dbPort = config.getString("database.port");
        dbUser = config.getString("database.username");
        dbPass = config.getString("database.password");
        dbDatabase = config.getString("database.database");
        dbPrefix = config.getString("database.prefix");
    }

    /**
     * Reloads settings
     */
    void reloadSettings() {
        plugin.reloadConfig();
        readSettings(plugin.getConfig());
    }
}
