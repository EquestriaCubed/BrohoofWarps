package com.brohoof.brohoofwarps;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class EventManager implements Listener {
    private Data d;

    public EventManager(Data data) {
        d = data;
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void updateNames(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        d.update(p.getUniqueId(), p.getName());
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
        if (sign.length > 0 && sign[0].equalsIgnoreCase("[Warp]")) {
            if (sign.length == 2) {
                // We just have a warp name, search to see if the clicker had a warp name.
                Optional<Warp> warp = d.getWarp(sign[1], e.getPlayer().getName());
                if (warp.isPresent()) {
                    // Sweet, we found it!
                    CommandExecutor.warp(p, p, warp.get());
                    return;
                }
                Warp[] warps = d.getWarps(sign[1]);
                if (warps.length == 0) {
                    p.sendMessage("§cThis warp doesn't exist!");
                    return;
                }
                if (warps.length == 1) {
                    CommandExecutor.warp(p, p, warps[0]);
                    return;
                }
                // Ambiguous!
                p.sendMessage("§cThis warp is ambiguous.");
                return;
            }
            Optional<Warp> warp = d.getWarp(sign[1], sign[2]);
            if (warp.isPresent()) {
                CommandExecutor.warp(p, p, warp.get());
                return;
            }
            p.sendMessage("§cThis warp doesn't exist!");
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onSignPlace(BlockPlaceEvent e) {
        
    }
}
