package com.brohoof.brohoofwarps;

import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class EventManager implements Listener {
    private Data d;
    private CommandExecutor ch;
    private BrohoofWarpsPlugin p;

    public EventManager(BrohoofWarpsPlugin pl, CommandExecutor cex, Data data) {
        p = pl;
        ch = cex;
        d = data;
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void updateNames(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        d.updateOwnerName(p.getUniqueId(), p.getName());
        update(p);
    }

    private void update(Player ply) {
        try {
            Block[] blocks = WarpAccess.getSigns(ply.getUniqueId());
            for (Block b : blocks) {
                if (b.getState() instanceof Sign) {
                    Sign sign = (Sign) b;
                    sign.setLine(3, ply.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onSignClick(PlayerInteractEvent e) {
        switch (e.getClickedBlock().getType()) {
            case SIGN:
            case WALL_SIGN:
            case SIGN_POST:
                break;
            default:
                return;
        }
        String[] sign = ((Sign) e.getClickedBlock().getState()).getLines();
        Player p = e.getPlayer();
        if (sign.length >= 3 && sign[0].equalsIgnoreCase("[Warp]")) {
            try {
                Optional<Warp> warp = WarpAccess.getWarp(sign[1], sign[2]);
                if (warp.isPresent()) {
                    Warp w = warp.get();
                    ch.warp(p, p, w);
                    return;
                }
                p.sendMessage(ChatColor.RED + sign[2] + " doesn't own a warp named " + sign[1]);
                return;
            } catch (Exception ex) {
                p.sendMessage(ChatColor.RED + "An internal exception occured while loading the warps. Please report this to an admin.");
                ex.printStackTrace();
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onSignPlace(SignChangeEvent e) {
        Player p = e.getPlayer();
        try {
            String[] lines = e.getLines();
            switch (lines.length) {
                case 0:
                case 1:
                    return;
                case 2: {
                    if (!lines[0].equalsIgnoreCase("[Warp]"))
                        return;
                    String warpName = lines[1];
                    String owner = p.getName();
                    Optional<Warp>[] warps = WarpAccess.getWarps(warpName);
                    Optional<Warp> warp = null;
                    for (Optional<Warp> warpa : warps) {
                        if (warpa.isPresent()) {
                            Warp w = warpa.get();
                            if (w.getAccess() == Access.GLOBAL)
                                warp = warpa;
                            if (warp == null)
                                warp = warpa;
                            if (warp.get().getOwner().equals(p.getUniqueId()))
                                warp = warpa;
                        }
                    }
                    if (warp.isPresent()) {
                        Warp w = warp.get();
                        warpName = w.getName();
                        owner = w.getOwnerName();
                        d.addSign(p.getUniqueId(), warpName);
                        e.setLine(1, warpName);
                        e.setLine(2, owner);
                        p.sendMessage(ChatColor.GREEN + "Found " + warpName + " by " + owner);
                        return;
                    }
                    p.sendMessage(ChatColor.RED + "Could not find " + warpName + "  with the owner " + owner);
                    return;
                }
                case 3: {
                    if (!lines[0].equalsIgnoreCase("[Warp]"))
                        return;
                    String warpName = lines[1];
                    String owner = lines[2];
                    Optional<Warp> warp = WarpAccess.getWarp(warpName, owner);
                    if (warp.isPresent()) {
                        Warp w = warp.get();
                        warpName = w.getName();
                        owner = w.getOwnerName();
                        d.addSign(w.getOwner(), warpName);
                        e.setLine(1, warpName);
                        e.setLine(2, owner);
                        p.sendMessage(ChatColor.GREEN + "Found " + warpName + " by " + owner);
                        return;
                    }
                    p.sendMessage(ChatColor.RED + "Could not find " + warpName + "  with the owner " + owner);
                    return;
                }
            }
        } catch (Exception ex) {
            p.sendMessage(ChatColor.RED + "An error occured while placing this warp sign.");
            ex.printStackTrace();
        }
    }

}
