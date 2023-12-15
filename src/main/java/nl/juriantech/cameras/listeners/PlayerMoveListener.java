package nl.juriantech.cameras.listeners;

import nl.juriantech.cameras.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final Main plugin;

    public PlayerMoveListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (plugin.getCameraManager().playerIsInCamera(player)) event.setCancelled(true);
    }
}
