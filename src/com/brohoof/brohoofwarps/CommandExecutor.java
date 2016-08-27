package com.brohoof.brohoofwarps;

import org.bukkit.command.CommandSender;

/**
 * The command executor class.
 *
 */
public class CommandExecutor {
    /**
     * Commands are delegated to this function
     * 
     * @param sender
     *            the sender that executed this command
     * @param args
     *            the commands input arguments
     * @return true if a valid command, otherwise false
     * @see org.bukkit.command.CommandExecutor#onCommand(CommandSender, org.bukkit.command.Command, String, String[])
     */
    public static boolean onCommand(CommandSender sender, String[] args) {
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
        return false;
    }
}
