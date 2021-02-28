package de.hglabor.plugins.uhc.game.mechanics;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GoldenHead implements Listener {
    public final static GoldenHead INSTANCE = new GoldenHead();
    private final ItemStack itemStack;

    private GoldenHead() {
        this.itemStack = new ItemBuilder(Material.GOLDEN_APPLE)
                .setName(ChatColor.GOLD + "Golden Head")
                .hideItemFlags()
                .hideEnchants()
                .setEnchantment(Enchantment.LURE, 1)
                .build();
    }

    @EventHandler
    private void onConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().isSimilar(itemStack)) {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 1));
        }
    }

    public void register() {
        NamespacedKey key = new NamespacedKey(Uhc.getPlugin(), "golden_head");
        ShapedRecipe recipe = new ShapedRecipe(key, itemStack);
        recipe.shape("GGG", "GHG", "GGG"); // haha ghg
        recipe.setIngredient('G', Material.GOLD_INGOT);
        recipe.setIngredient('H', Material.PLAYER_HEAD);
        Uhc.getPlugin().getServer().addRecipe(recipe);
    }

    public ItemStack getHeadItem() {
        return itemStack;
    }
}
