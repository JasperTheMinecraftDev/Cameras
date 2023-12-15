package nl.juriantech.cameras.objects;

import org.bukkit.Location;

import java.util.UUID;

public class Camera {
    private final UUID id;
    private final String name;
    private final UUID owner;
    private final SimpleLocation simpleLocation;

    public Camera(UUID id, String name, UUID owner, SimpleLocation loc) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.simpleLocation = loc;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getOwner() {
        return owner;
    }

    public SimpleLocation getSimpleLocation() {
        return simpleLocation;
    }

    public Location getLocation() {
        return simpleLocation.toLocation();
    }
}
