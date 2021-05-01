package de.hglabor.plugins.uhc.game.phases;

import de.hglabor.plugins.uhc.game.GameManager;
import de.hglabor.plugins.uhc.game.GamePhase;
import de.hglabor.plugins.uhc.game.PhaseType;
import de.hglabor.plugins.uhc.game.mechanics.border.Border;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.utils.noriskutils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class PvPPhase extends IngamePhase {
    protected PvPPhase() {
        super(0, PhaseType.PVP);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void tick(int timer) {
        Border border = GameManager.INSTANCE.getBorder();
        border.announceBorderShrink(timer);
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (border.getBorderSize() > border.getShortestBorderSize()) {
                player.sendActionBar("N\u00E4chster Bordershrink auf " + border.getNextBorderSize() + " in: " + TimeConverter.stringify(border.getNextShrinkTime() - timer));
            }
        });
        if (timer == border.getNextShrinkTime()) {
            border.run();
        }
    }

    @Override
    public String getTimeString(int timer) {
        if (timer >= 3600) {
            return ChatColor.AQUA + "Duration: " + ChatColor.GREEN + TimeConverter.stringify(timer, "%02d:%02d:%02d");
        } else {
            return ChatColor.AQUA + "Duration: " + ChatColor.GREEN + TimeConverter.stringify(timer);
        }
    }

    @Override
    protected GamePhase getNextPhase() {
        return null;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            UHCPlayer player = playerList.getPlayer((Player) event.getEntity());
            if (player.isTeleporting()) event.setCancelled(true);
        }
    }
}
