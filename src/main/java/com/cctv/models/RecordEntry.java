package com.cctv.models;

public class RecordEntry {

    private final long offsetMillis; // time since recording started
    private final String playerName;
    private final String world;
    private final double x, y, z;
    private final float yaw, pitch;

    public RecordEntry(long offsetMillis, String playerName, String world,
                        double x, double y, double z, float yaw, float pitch) {
        this.offsetMillis = offsetMillis;
        this.playerName = playerName;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public long getOffsetMillis() { return offsetMillis; }
    public String getPlayerName() { return playerName; }
    public String getWorld() { return world; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
}
