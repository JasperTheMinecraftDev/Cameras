package nl.juriantech.cameras;

import nl.juriantech.cameras.commands.CameraCommand;
import nl.juriantech.cameras.listeners.PlayerInteractListener;
import nl.juriantech.cameras.listeners.PlayerDamageListener;
import nl.juriantech.cameras.listeners.PlayerMoveListener;
import nl.juriantech.cameras.listeners.PlayerQuitListener;
import nl.juriantech.cameras.managers.CameraManager;
import nl.juriantech.cameras.runnables.DistanceRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public final class Main extends JavaPlugin {

    private Storage storage;
    private CameraManager cameraManager;
    private DistanceRunnable distanceRunnable;
    public int cameraLimitPerPlayer = 5;

    @Override
    public void onEnable() {
        // Plugin startup logic
        storage = new Storage(this);
        storage.load();
        cameraManager = new CameraManager(this);
        listeners();
        commands();
        runnables();
        getLogger().info("The camera plugin is successfully enabled.");
    }

    private void runnables() {
        distanceRunnable = new DistanceRunnable(this);
        distanceRunnable.runTaskTimer(this, 20, 20);
    }

    private void commands() {
        BukkitCommandHandler handler = BukkitCommandHandler.create(this);
        handler.register(new CameraCommand(this));
    }

    private void listeners() {
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (cameraManager.playerIsInCamera(player)) {
                cameraManager.stopSpectating(player);
            }
        }

        distanceRunnable.cancel();
        storage.save();
        storage = null;
        cameraManager = null;
        getLogger().info("The camera plugin is successfully disabled.");
    }

    public Storage getStorage() {
        return storage;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }
}
