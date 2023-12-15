package nl.juriantech.cameras.commands;

import nl.juriantech.cameras.Main;
import nl.juriantech.cameras.objects.Camera;
import nl.juriantech.cameras.objects.SimpleLocation;
import nl.juriantech.cameras.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.List;
import java.util.UUID;

@Command({"camera", "cam"})
public class CameraCommand {

    private final Main plugin;

    public CameraCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Subcommand("create")
    public void onCreate(Player player, String name) {
        if (plugin.getCameraManager().getPlayerCameras(player).size() == plugin.cameraLimitPerPlayer) {
            player.sendMessage(ChatUtils.colorize("&cYou reached the maximum(" + plugin.cameraLimitPerPlayer + ") amount of camera's, you can't create more.."));
            return;
        }

        for (Camera camera : plugin.getCameraManager().getPlayerCameras(player)) {
            if (camera.getName().equals(name)) {
                player.sendMessage(ChatUtils.colorize("&cYou already have a camera with this name, please use another one.."));
                return;
            }
        }

        plugin.getStorage().getCameras().add(new Camera(UUID.randomUUID(), name, player.getUniqueId(), SimpleLocation.fromLocation(player.getLocation())));
        player.sendMessage(ChatUtils.colorize("&2You successfully created a camera with the name &f&l" + name + "&2&l on your current location!"));
    }
    @Subcommand("list")
    @CommandPermission("camera.list")
    public void onList(Player player) {
        List<Camera> playerCameras = plugin.getCameraManager().getPlayerCameras(player);
        if (playerCameras.isEmpty()) {
            player.sendMessage(ChatUtils.colorize("&3You don't have camera's yet."));
            return;
        }

        player.sendMessage(ChatUtils.colorize("&3Your camera's:"));
        for (Camera camera : playerCameras) {
            player.sendMessage(ChatUtils.colorize("&3 - " + camera.getName()));
        }
    }

    @Subcommand("view")
    @CommandPermission("camera.view")
    public void onView(Player player, String name) {
        Camera camera = plugin.getCameraManager().getPlayerCamera(player, name);

        if (camera == null) {
            player.sendMessage(ChatUtils.colorize("&cYou don't have a camera with this name."));
            return;
        }

        if (player.getLocation().distance(camera.getLocation()) > 500) {
            player.sendMessage(ChatUtils.colorize("&cThis camera is too far away, you can't view it."));
            return;
        }

        if (plugin.getCameraManager().playerIsInCamera(player)) {
            player.sendMessage(ChatUtils.colorize("&cYou are already spectating a camera!"));
            return;
        }

        Location cameraLocation = camera.getLocation();
        if (cameraLocation != null) {
            plugin.getCameraManager().spectateCamera(player, camera);
            player.sendMessage(ChatUtils.colorize("&2Je bekijkt nu de camera &f" + name + "."));
        } else {
            player.sendMessage(ChatUtils.colorize("&cThe location of this camera is unavailable."));
        }
    }

    @Subcommand("delete")
    @CommandPermission("camera.delete")
    public void onDelete(Player player, String name) {
        Camera camera = plugin.getCameraManager().getPlayerCamera(player, name);

        if (camera == null) {
            player.sendMessage(ChatUtils.colorize("&cThere is no camera with that name."));
            return;
        }

        plugin.getStorage().getCameras().remove(camera);
        player.sendMessage(ChatUtils.colorize("&2You deleted the &f" + name + "&2 camera."));
    }
}