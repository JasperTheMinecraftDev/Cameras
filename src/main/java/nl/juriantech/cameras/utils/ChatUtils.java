package nl.juriantech.cameras.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ChatUtils {

    public static String colorize(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendTitle(final Player player, final String message, long fadeIn, long stay, long fadeOut) {
        player.sendTitle(colorize(message), "", (int) fadeIn, (int) stay, (int) fadeOut);
    }

    public static void sendActionBarMessage(final Player player, final String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(colorize(message)));
    }
}
