package com.brohoof.brohoofwarps;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.World;

/**
 * A warp stores data about warps, and allows users to warp to them. Allows for translation between memory objects and SQL data storage
 *
 */
public class Warp {
    /**
     * The name of this warp
     */
    private String name;
    /**
     * The name of the player who owns the warp.
     */
    private String ownerName;
    /**
     * The {@link UUID} of the player who owned it.
     * 
     * @see org.bukkit.entity.Player
     */
    private UUID owner;
    /**
     * An array containing a list of all the UUIDs of people who are allowed to access the warp, if it is private
     */
    private Invite[] invitees;
    /**
     * The World this warp is located in. If the Optional is empty, the world is not loaded.
     */
    private Optional<World> world;
    /**
     * The x component of the warp location.
     */
    private double x;
    /**
     * The y component of the warp location.
     */
    private double y;
    /**
     * The z component of the warp location.
     */
    private double z;
    /**
     * The pitch component of the warp location.
     */
    private float pitch;
    /**
     * The yaw component of the warp location.
     */
    private float yaw;
    /**
     * The access modifier to use for this warp. 0 means global, 1 means public, and 2 means private.
     */
    private int access;

    /**
     * Creates a new Warp object.
     * 
     * @param name
     *            The name of this warp
     * @param ownerName
     *            The name of the player who owns the warp.
     * @param owner
     *            The {@link UUID} of the player who owned it.
     * @param invitees
     *            An array containing a list of all the UUIDs of people who are allowed to access the warp, if it is private
     * @param world
     *            The string name of the world. To be saved in an optional later
     * @param x
     *            The x component of the warp location.
     * @param y
     *            The y component of the warp location.
     * @param z
     *            The z component of the warp location.
     * @param yaw
     *            The yaw component of the warp location.
     * @param pitch
     *            The pitch component of the warp location.
     * @param access
     *            The access modifier to use for this warp. 0 means global, 1 means public, and 2 means private.
     */
    // What if instead of having an Optional<World> inside the constructor, we gave it a string, and told it the main thread to get the Optional later?
    public Warp(String name, String ownerName, UUID owner, Invite[] invitees, String world, final double x, final double y, final double z, final float yaw, final float pitch, int access) {
        this.name = name;
        this.ownerName = ownerName;
        this.owner = owner;
        this.invitees = invitees;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.access = access;
        getWorldLater(world);
    }

    /**
     * Worker method to create the Optional object of the location component
     * 
     * @param worldName
     *            the string name of the world
     */
    private void getWorldLater(String worldName) {
    }

    /**
     * 
     * @return the name of this warp
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return the owner's name of this warp
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * 
     * @return the owner's UUID of this warp
     */
    public UUID getOwner() {
        return owner;
    }

    /**
     * 
     * @return the invites to this warp
     */
    public Invite[] getInvitees() {
        return invitees;
    }

    /**
     * Returns the world. This is an Optional, because it may or may not have a loaded world!
     * If the world is loaded, the Optional will have the world.
     * 
     * @return the world
     */
    public Optional<World> getWorld() {
        return world;
    }

    /**
     * 
     * @return The x component of the warp location.
     */
    public double getX() {
        return x;
    }

    /**
     * 
     * @return The y component of the warp location.
     */
    public double getY() {
        return y;
    }

    /**
     * 
     * @return The z component of the warp location.
     */
    public double getZ() {
        return z;
    }

    /**
     * 
     * @return The pitch component of the warp location.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * 
     * @return The yaw component of the warp location.
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * 
     * @return The access modifier to use for this warp. 0 means global, 1 means public, and 2 means private.
     */
    public int getAccess() {
        return access;
    }
}
