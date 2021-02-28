package de.hglabor.plugins.uhc.game;

import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.game.config.CKeys;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public abstract class Scenario implements Listener {
    private final String name;
    private final ItemStack displayItem;
    private boolean isEnabled;

    public Scenario(String name, ItemStack displayItem) {
        this.name = name;
        this.displayItem = displayItem;
        saveToConfig();
    }

    protected void saveToConfig() {
        Uhc plugin = Uhc.getPlugin();
        plugin.getConfig().addDefault(CKeys.SCENARIOS + "." + getName() + "." + "enabled", false);
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }

    protected void loadConfig() {
    }


    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getName() {
        return name;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }
}
