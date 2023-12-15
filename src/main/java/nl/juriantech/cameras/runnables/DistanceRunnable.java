package nl.juriantech.cameras.runnables;

import nl.juriantech.cameras.Main;
import nl.juriantech.cameras.objects.Camera;
import nl.juriantech.cameras.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class DistanceRunnable extends BukkitRunnable {

    private final Main plugin;

    public DistanceRunnable(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ArrayList<Camera> playerCameras = plugin.getCameraManager().getPlayerCameras(player);
            int unreachableCameras = 0;

            for (Camera camera : playerCameras) {
                if (player.getLocation().distance(camera.getLocation()) > 500) {
                    unreachableCameras++;
                }
            }

            if (!playerCameras.isEmpty()) {
                int reachableCameras = playerCameras.size() - unreachableCameras;
                String actionBarMessage = String.format("&bBereikbare camera's: &f%d/%d", reachableCameras, playerCameras.size());
                ChatUtils.sendActionBarMessage(player, actionBarMessage);
            }
        }
    }
}
