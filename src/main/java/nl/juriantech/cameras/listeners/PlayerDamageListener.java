package nl.juriantech.cameras.listeners;

import nl.juriantech.cameras.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {
    private final Main plugin;

    public PlayerDamageListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (plugin.getCameraManager().playerIsInCamera(player)) event.setCancelled(true);
        }
    }
}
