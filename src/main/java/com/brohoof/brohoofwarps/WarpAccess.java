package com.brohoof.brohoofwarps;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

@SuppressWarnings("deprecation")
public class WarpAccess {
	
	private static BrohoofWarpsPlugin p;
	
	static void setPlugin(BrohoofWarpsPlugin p) {
		if (WarpAccess.p != null)
			throw new UnsupportedOperationException("Cannot redefine plugin.");
		WarpAccess.p = p;
	}
	
	public static Optional<Warp> getWarp(String warpName, String ownerName) throws InterruptedException {
		WarpWorker<Optional<Warp>> worker = new WarpWorker<Optional<Warp>>("Warp", warpName, ownerName);
		Bukkit.getScheduler().scheduleAsyncDelayedTask(p, worker);
		return worker.get();
	}

	public static Optional<Warp>[] getWarps(String warpName) throws InterruptedException {
		WarpWorker<Optional<Warp>[]> worker = new WarpWorker<Optional<Warp>[]>("Warp[]", warpName, null);
		Bukkit.getScheduler().scheduleAsyncDelayedTask(p, worker);
		return worker.get();
	}
	

	public static Block[] getSigns(UUID signOwner) throws InterruptedException, InvalidWarpStateException {
		WarpWorker<WarpSign[]> worker = new WarpWorker<WarpSign[]>("Sign", null, signOwner);
		Bukkit.getScheduler().scheduleAsyncDelayedTask(p, worker);
		WarpSign[] signs = worker.get();
		ArrayList<Block> blocks = new ArrayList<Block>(0);
		for (WarpSign loc : signs) {
			loc.setWorld(Optional.<World>ofNullable(Bukkit.getWorld(loc.getWorldName())));
			blocks.add(loc.buildLocation().getBlock());
		}
		return null;
	}
}
