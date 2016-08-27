package com.brohoof.brohoofwarps;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExecutor {
    private Data data;
    private Settings settings;
    private BrohoofWarpsPlugin plugin;

    public CommandExecutor(BrohoofWarpsPlugin brohoofWarpsPlugin, Data d, Settings s) {
        this.plugin = brohoofWarpsPlugin;
        this.data = d;
        this.settings = s;
    }

    /**
     * Processes commands
     * 
     * @param sender
     *            the sender
     * @param args
     *            the arguments
     * @return weather the command was successful or not
     */
    public boolean execute(CommandSender sender, String[] args) {
        // Format is always /warp list, /warp search, /warp create, /warp delete, or just /warp warpname
        if (args.length == 0)
            return false;
        if (args[0].equalsIgnoreCase("list")) {
            // Just list all the warps in the database.
            // USE AN ASYNC TASK
            return true;
        }
        if (args[0].equalsIgnoreCase("search")) {
            // Search for warps in the database
            // USE AN ASYNC TASK
            return true;
        }
        if (args[0].equalsIgnoreCase("create")) {
            // Simply sets a warp
            return true;
        }
        if (args[0].equalsIgnoreCase("delete")) {
            // Simply deletes a warp
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            // Prints a help menu
            return true;
        }
        if (sender instanceof Player) {
            // Okay, right off the bat, we know that the person wanting to warp is a player. So we know that they might own warps.
            Player p = (Player) sender;
            Optional<Warp> warp;
            warp = data.getWarp(args[0], p.getName());
            return true;
        }
        // The sender is not a player, they are either console, or someone else They cannot warp themselves, only others.
        String warpName = args[0];
        String warpOwner = args[1];
        Player target = getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage("§cThat player is not online.");
        }
        Optional<Warp> warp = data.getWarp(warpName, warpOwner);
        if (!warp.isPresent()) {
            sender.sendMessage("§c" + warpOwner + " doesn't own a warp named " + warpName);
            return true;
        }
        warp(sender, target, warp.get());
        return false;
    }

    public static void warp(CommandSender sender, Player target, Warp warp) {
        if (warp.getWorld().isPresent()) {
            target.teleport(new Location(warp.getWorld().get(), warp.getX(), warp.getY(), warp.getZ(), warp.getYaw(), warp.getPitch()));
            target.sendMessage("§bWarped to " + warp.getName() + ".");
        } else {
            sender.sendMessage("§cThe world this warp is located in is not loaded.");
        }
    }

    private Player getPlayer(String player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(player))
                return p;
        }
        return null;
    }
}
