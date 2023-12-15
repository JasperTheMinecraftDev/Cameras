package nl.juriantech.cameras.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SimpleLocation {
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public SimpleLocation(World world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public World getWorld() {
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

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void teleport(Player player) {
        Location location = toLocation();
        if (location != null) {
            player.teleport(location);
        }
    }

    public Location toLocation() {
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return world.getName() + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
    }

    public static SimpleLocation fromString(String str) {
        String[] parts = str.split(":");
        if (parts.length == 6) {
            World world = Bukkit.getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);

            return new SimpleLocation(world, x, y, z, yaw, pitch);
        }
        return null;
    }

    public static SimpleLocation fromLocation(Location loc) {
        return new SimpleLocation(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }
}
