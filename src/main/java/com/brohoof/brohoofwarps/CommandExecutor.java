package com.brohoof.brohoofwarps;

import java.util.ArrayList;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {
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
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            // Format is always /warp list, /warp search, /warp create, /warp
            // delete, or just /warp warpname
            if (args.length == 0)
                return false;
            switch (args[0].toLowerCase()) {
                case "list": {
                    return true;
                }
                case "search":
                case "create":
                case "createp":
                case "createg":
                case "delete":
                case "update": {
                    if (sender instanceof Player) {
                        Player ply = (Player) sender;
                        if (args.length == 1) {
                            Optional<Warp> warpToUpdate = WarpAccess.getWarp(args[0], ply.getName());
                            if (warpToUpdate.isPresent()) {
                                Location plyLoc = ply.getLocation();
                                data.updateLocation(warpToUpdate.get(), plyLoc.getX(), plyLoc.getY(), plyLoc.getZ(), plyLoc.getPitch(), plyLoc.getYaw());
                                ply.sendMessage(ChatColor.AQUA + "Updated warp " + args[0]);
                                return true;
                            }
                            Optional<Warp>[] warps = WarpAccess.getWarps(args[0]);
                            if(warps.length == 0) {
                                ply.sendMessage(ChatColor.GREEN + "You need to enter a warp to update! Type /warp help for help.");
                                return true;
                            }
                            if(warps.length > 1) {
                                ply.sendMessage(ChatColor.RED + "This warp is ambiguous.");
                                return true;
                            }
                            if(ply.hasPermission("warps.update.other")) {
                                Location plyLoc = ply.getLocation();
                                data.updateLocation(warpToUpdate.get(), plyLoc.getX(), plyLoc.getY(), plyLoc.getZ(), plyLoc.getPitch(), plyLoc.getYaw());
                                ply.sendMessage(ChatColor.AQUA + "Updated warp " + args[0]);
                                return true;
                            }
                            ply.sendMessage(ChatColor.RED + "You do not have permission to warp to this warp.");
                        }
                        if (args.length == 2) {
                        }
                    }
                }
                case "help":
                default: {
                    if (sender instanceof Player) {
                        // Okay, right off the bat, we know that the person wanting
                        // to warp is a player. So we know that they might own
                        // warps.
                        Player warper = (Player) sender;
                        switch (args.length) {
                            case 0: {
                                warper.sendMessage(ChatColor.GREEN + "You need to enter a warp to visit! Type /warp help for help.");
                                return true;
                            }
                            case 1: {
                                Optional<Warp>[] warps = WarpAccess.getWarps(args[0]);
                                ArrayList<Warp> validWarps = new ArrayList<Warp>(0);
                                for (Optional<Warp> ow : warps) {
                                    if (ow.isPresent()) {
                                        Warp w = ow.get();
                                        w.setWorld(Optional.<World>ofNullable(Bukkit.getWorld(w.getWorldName())));
                                        validWarps.add(w);
                                    }
                                }
                                Optional<Warp> possibleOwner = WarpAccess.getWarp(args[0], warper.getName());
                                if (possibleOwner.isPresent())
                                    validWarps.add(0, possibleOwner.get());
                                if (validWarps.get(0).getOwner().equals(warper.getUniqueId())) {
                                    Warp warpTo = validWarps.get(0);
                                    warpTo.setWorld(Optional.<World>ofNullable(Bukkit.getWorld(warpTo.getWorldName())));
                                    warp(warper, warper, warpTo);
                                    return true;
                                }
                                for (Warp warp : validWarps) {
                                    if (warp.getAccess() == Access.GLOBAL) {
                                        warp(warper, warper, warp);
                                        return true;
                                    }
                                }
                                warper.sendMessage(ChatColor.RED + "This warp is ambiguous.");
                            }
                            case 2: {
                                Optional<Warp> warpToO = WarpAccess.getWarp(args[0], args[1]);
                                if (warpToO.isPresent()) {
                                    Warp warpTo = warpToO.get();
                                    warpTo.setWorld(Optional.<World>ofNullable(Bukkit.getWorld(warpTo.getWorldName())));
                                    warp(warper, warper, warpTo);
                                    return true;
                                }
                            }
                            case 3: {
                                Optional<Player> target = getPlayerTarget(args[2]);
                                if (!target.isPresent()) {
                                    sender.sendMessage(ChatColor.RED + "Cannot find player " + args[2]);
                                    return true;
                                }
                                Optional<Warp> warpToO = WarpAccess.getWarp(args[0], args[1]);
                                if (warpToO.isPresent()) {
                                    Warp warpTo = warpToO.get();
                                    warpTo.setWorld(Optional.<World>ofNullable(Bukkit.getWorld(warpTo.getWorldName())));
                                    warp(warper, target.get(), warpTo);
                                    return true;
                                }
                            }

                        }
                        return false;

                    } // The sender is not a player, they are either console, or someone else They cannot warp themselves, only others.
                }
            }

            return false;
        } catch (

        Exception ex) {
            sender.sendMessage(ChatColor.RED + "An error occured while executing this command.");
            ex.printStackTrace();
            return true;
        }
    }

    public void warp(CommandSender sender, Player target, Warp warp) {
        if (sender instanceof Player) {
            if (warp.getAccess() == Access.PRIVATE && (sender.hasPermission("warp.invite.override") || ((Player) sender).getUniqueId().equals(warp.getOwner()) || warp.getInvitees().contains(((Player) sender).getUniqueId()))) {
                if (warp.getWorld().isPresent()) {
                    target.teleport(new Location(warp.getWorld().get(), warp.getX(), warp.getY(), warp.getZ(), warp.getYaw(), warp.getPitch()));
                    target.sendMessage(ChatColor.AQUA + "Warped to " + warp.getName() + ".");
                } else {
                    sender.sendMessage(ChatColor.RED + "The world this warp is located in is not loaded.");
                }
            } else {
                target.sendMessage(ChatColor.RED + "You do not have permission to warp to this warp.");
            }
        } else {
            if (warp.getWorld().isPresent()) {
                target.teleport(new Location(warp.getWorld().get(), warp.getX(), warp.getY(), warp.getZ(), warp.getYaw(), warp.getPitch()));
                target.sendMessage(ChatColor.AQUA + "Warped to " + warp.getName() + ".");
            } else {
                sender.sendMessage(ChatColor.RED + "The world this warp is located in is not loaded.");
            }
        }
    }

    private Optional<Player> getPlayerTarget(String player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(player))
                return Optional.<Player>of(p);
        }
        return Optional.<Player>empty();
    }
}
