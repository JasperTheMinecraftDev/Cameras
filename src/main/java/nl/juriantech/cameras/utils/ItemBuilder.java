package nl.juriantech.cameras.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is a chainable builder for {@link ItemStack}s in {@link Bukkit}
 * <br>
 * Example Usage:<br>
 * {@code ItemStack is = new ItemBuilder(Material.LEATHER_HELMET).amount(2).data(4).durability(4).enchantment(Enchantment.ARROW_INFINITE).enchantment(Enchantment.LUCK, 2).name(ChatColor.RED + "the name").lore(ChatColor.GREEN + "line 1").lore(ChatColor.BLUE + "line 2").color(Color.MAROON).build();}
 *
 * @author MiniDigger
 * @version 1.2
 */
public class ItemBuilder {
    private final ItemStack item;

    private String name;
    private boolean hideAttributes;
    private List<String> lore = new ArrayList<String>();
    private UUID skullOwner;
    private String skullTexture;

    /**
     * Creates a new ItemBuilder with the specified material.
     *
     * @param material The material
     */
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount Item amount
     * @return This ItemBuilder
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Sets the name of the item.
     *
     * @param name Item name
     * @return This ItemBuilder
     */
    public ItemBuilder displayName(String name) {
        this.name = ChatUtils.colorize(name);
        return this;
    }

    /**
     * Adds an empty lore line.
     * This can be used to skip a line,
     * a.k.a. a spacer.
     *
     * @return This ItemBuilder
     */
    public ItemBuilder addLoreSpacer() {
        this.lore.add("");
        return this;
    }

    /**
     * Adds the specified lore line to the
     * item. If there are newline characters
     * in the string, multiple lines are added.
     *
     * @param lore Line to add to the lore.
     * @return This ItemBuilder
     */
    public ItemBuilder lore(String lore) {
        String[] lores = lore.split("\n");
        for(String splitLore : lores) {
            this.lore.add(ChatUtils.colorize(splitLore));
        }
        return this;
    }

    /**
     * Sets the skull owner of the item to the specified player.
     *
     * @param player The player's name or UUID
     * @return This ItemBuilder
     */
    public ItemBuilder setSkullOwner(UUID player) {
        this.skullOwner = player;
        return this;
    }

    /**
     * Hides this item's attributes, like damage
     * stats on swords.
     *
     * @return This ItemBuilder
     */
    public ItemBuilder hideAttributes() {
        this.hideAttributes = true;
        return this;
    }

    /**
     * Sets the skull owner of the item to the specified player.
     *
     * @param owner The player's name or UUID
     * @return This ItemBuilder
     */
    public ItemBuilder setSkullOwner(String owner) {
        this.skullTexture = null; // Reset skull texture if it was set before

        try {
            UUID uuid;
            OfflinePlayer offlinePlayer;

            // Try parsing UUID directly
            try {
                uuid = UUID.fromString(owner);
                offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            } catch (IllegalArgumentException e) {
                // If it's not a UUID, try getting the player by name
                offlinePlayer = Bukkit.getOfflinePlayer(owner);
                uuid = offlinePlayer.getUniqueId();
            }

            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            skullMeta.setOwningPlayer(offlinePlayer);
            item.setItemMeta(skullMeta);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Sets the custom texture for the player head.
     *
     * @param texture The Base64-encoded texture string
     * @return This ItemBuilder
     */
    public ItemBuilder setSkullTexture(String texture) {
        this.skullTexture = texture;
        return this;
    }

    /**
     * Returns an ItemStack with the settings
     * specified in this ItemBuilder.
     * <p>
     * This returns a clone, so modifying
     * the ItemStack further afterwards does
     * not affect this ItemBuilder.
     *
     * @return A clone of the ItemStack
     */
    public ItemStack build() {
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (name != null) {
                meta.setDisplayName(name);
            }

            if (lore != null) {
                meta.setLore(lore);
            }

            if (hideAttributes) {
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }

            item.setItemMeta(meta);

            if (skullTexture != null) {
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                profile.getProperties().put("textures", new Property("textures", skullTexture));
                setGameProfile(skullMeta, profile);
                item.setItemMeta(skullMeta);
            }
        }
        return item.clone();
    }

    // Method to set GameProfile in SkullMeta
    private void setGameProfile(SkullMeta skullMeta, GameProfile profile) {
        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static ItemBuilder from(String string) {
        String[] parts = string.split(":");
        String material = parts[0];
        String displayName = parts[1];
        String lore = parts[2];

        return new ItemBuilder(Material.valueOf(material)).displayName(displayName).lore(lore);
    }
}