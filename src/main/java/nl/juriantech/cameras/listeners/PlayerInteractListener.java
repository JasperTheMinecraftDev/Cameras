package nl.juriantech.cameras.listeners;

import nl.juriantech.cameras.Main;
import nl.juriantech.cameras.utils.ChatUtils;
import nl.juriantech.cameras.utils.PlayerUitls;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerInteractListener implements Listener {
    private final Main plugin;

    public PlayerInteractListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        Player player = event.getPlayer();
        int slot = player.getInventory().getHeldItemSlot();

        if (plugin.getCameraManager().playerIsInCamera(player)) {
            switch (slot) {
                case 0:
                    if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    } else {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999999, 99));
                    }
                    break;
                case 3:
                    ChatUtils.sendActionBarMessage(player, "&6Naar links aan het draaien..");
                    PlayerUitls.rotatePlayerHead(player, -15.0f);
                    break;
                case 5:
                    ChatUtils.sendActionBarMessage(player, "&6Naar rechts aan het draaien..");
                    PlayerUitls.rotatePlayerHead(player, 15.0f);
                    break;
                case 8:
                    plugin.getCameraManager().stopSpectating(player);
                    break;
                default:
                    break;
            }

            event.setCancelled(true);
        }
    }
}
