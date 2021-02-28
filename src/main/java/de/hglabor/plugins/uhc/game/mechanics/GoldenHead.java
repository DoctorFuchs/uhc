package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.Uhc;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GoldenHead implements Listener {

    public static ItemStack itemStack;

    public static void register() {
        registerCraftingRecipe();

        ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Golden Head");
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.LURE, 1);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack = item;
    }

    public static void registerCraftingRecipe() {
        NamespacedKey key = new NamespacedKey(Uhc.getPlugin(), "golden_head");
        ShapedRecipe recipe = new ShapedRecipe(key, itemStack);
        recipe.shape("GGG", "GHG", "GGG"); // haha ghg
        recipe.setIngredient('G', Material.GOLD_INGOT);
        recipe.setIngredient('H', Material.PLAYER_HEAD);

        Uhc.getPlugin().getServer().addRecipe(recipe);
    }

    @EventHandler
    public static void onConsume(PlayerItemConsumeEvent event) {
        ItemMeta meta = event.getItem().getItemMeta();
        if (!meta.hasDisplayName()) {
            return;
        }
        if (meta.getDisplayName().equals(ChatColor.GOLD + "Golden Head")) {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 9, 1));
        }
    }
}
