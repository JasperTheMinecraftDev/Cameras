package nl.juriantech.cameras.managers;

import nl.juriantech.cameras.Main;
import nl.juriantech.cameras.objects.Camera;
import nl.juriantech.cameras.objects.PlayerInformation;
import nl.juriantech.cameras.utils.ChatUtils;
import nl.juriantech.cameras.utils.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CameraManager {

    private final Main plugin;
    private final HashMap<UUID, PlayerInformation> playersSpectatingCamera;

    public CameraManager(Main plugin) {
        this.plugin = plugin;
        this.playersSpectatingCamera = new HashMap<>();
    }

    public void spectateCamera(final Player player, final Camera camera) {
        if (playersSpectatingCamera.containsKey(player.getUniqueId())) return;
        if (!camera.getOwner().equals(player.getUniqueId())) return;

        ChatUtils.sendTitle(player,"&2Verbinden...", 10, 60, 10);

        new BukkitRunnable() {
            public void run() {
                playersSpectatingCamera.put(player.getUniqueId(), new PlayerInformation(player));
                player.setGameMode(GameMode.ADVENTURE);
                player.setAllowFlight(true);
                player.setFlying(true);
                player.teleport(camera.getLocation());
                giveItems(player);
            }
        }.runTaskLater(plugin, 60);
    }

    public void stopSpectating(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
        playersSpectatingCamera.get(player.getUniqueId()).restore();
        playersSpectatingCamera.remove(player.getUniqueId());
    }

    public void giveItems(Player player) {
        //night vision toggle
        player.getInventory().setItem(0, new ItemBuilder(Material.REDSTONE_LAMP).displayName("&6Night vision togglen").build());
        //arrow to the left player head
        player.getInventory().setItem(3, new ItemBuilder(Material.PLAYER_HEAD).displayName("&bNaar links draaien").setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDAyY2YwZGVhNGQwNmVmMWRlMjU1YmI4MTVmNjQyNjhkNjhiYjNiOTAzZDYwNjIxYWU5MmUwMDdlMjg4MTk4YyJ9fX0=").build());
        //arrow to the right player head
        player.getInventory().setItem(5, new ItemBuilder(Material.PLAYER_HEAD).displayName("&bNaar rechts draaien").setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVkYTE4NDU0NmEwNWRlOTZhNGQxZmI4MjBmNzJjMDlmMTU5N2RmNTZkODhhNGFiMzVlOWI0YTJjNzgwYWI4NCJ9fX0=").build());
        //item to stop spectating
        player.getInventory().setItem(8, new ItemBuilder(Material.BARRIER).displayName("&cStoppen met het bekijken van deze camera.").build());
    }

    public boolean playerIsInCamera(Player player) {
        return playersSpectatingCamera.containsKey(player.getUniqueId());
    }

    public ArrayList<Camera> getPlayerCameras(Player player) {
        ArrayList<Camera> array = new ArrayList<>();

        for (Camera camera : plugin.getStorage().getCameras()) {
            if (camera.getOwner().equals(player.getUniqueId())) array.add(camera);
        }

        return array;
    }

    public Camera getPlayerCamera(Player player, String name) {
        Camera camera = null;
        for (Camera cam : plugin.getCameraManager().getPlayerCameras(player)) {
            if (cam.getName().equalsIgnoreCase(name)) {
                camera = cam;
                break;
            }
        }

        return camera;
    }
}