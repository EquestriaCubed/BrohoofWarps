package com.brohoof.brohoofwarps;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.World;

public class Warp {
    private String name;
    private String ownerName;
    private UUID owner;
    private Invite[] invitees;
    private Optional<World> world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    // What if instead of having an Optional<World> inside the constructor, we gave it a string, and told it the main thread to get the Optional later?
    public Warp(String name, String ownerName, UUID owner, Invite[] invitees, String world, final double x, final double y, final double z, final float yaw, final float pitch) {
        this.name = name;
        this.ownerName = ownerName;
        this.owner = owner;
        this.invitees = invitees;
        //this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public String getName() {
        return name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public UUID getOwner() {
        return owner;
    }

    public Invite[] getInvitees() {
        return invitees;
    }

    /**
     * Returns the world. This is an Optional, because it may or may not have a loaded world!
     * If the world is loaded, the Optional will have the world.
     * 
     * @return
     */
    public Optional<World> getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
