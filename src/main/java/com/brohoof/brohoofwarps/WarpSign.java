package com.brohoof.brohoofwarps;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;

public class WarpSign {

	private Optional<World> world;
	private String worldName;
	private int x;
	private int y;
	private int z;

	public WarpSign(String worldName, int x, int y, int z) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		world = Optional.<World>empty();
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorld(Optional<World> world) {
		Validate.notNull(world);
		this.world = world;
	}

	/**
	 * This method should only be called from the main bukkit thread.
	 * 
	 * @return the Location that this object has.
	 */
	public Location buildLocation() throws InvalidWarpStateException {
		try {
			return new Location(world.get(), x, y, z);
		} catch (NoSuchElementException ex) {
			throw new InvalidWarpStateException("The Location that was requested does not exist, because the world is not loaded. The world loaded state was not checked.", ex);
		}
	}

}
