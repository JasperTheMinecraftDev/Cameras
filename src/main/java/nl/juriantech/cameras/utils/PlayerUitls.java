package nl.juriantech.cameras.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerUitls {

    /*
     * Supply a positive offset to turn it for the right and a negative offset to turn it to the left.
     */
    public static void rotatePlayerHead(Player player, float offset) {
        float newYaw = (player.getLocation().getYaw() + offset) % 360.0f;
        Location newLoc = player.getLocation().clone();
        newLoc.setYaw(newYaw);

        player.teleport(newLoc);
    }
}
