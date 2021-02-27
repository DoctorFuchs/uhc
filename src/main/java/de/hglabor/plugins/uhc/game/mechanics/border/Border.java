package de.hglabor.plugins.uhc.game.mechanics.border;

import com.google.common.collect.ImmutableMap;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import de.hglabor.plugins.uhc.game.config.CKeys;
import de.hglabor.plugins.uhc.game.config.UHCConfig;
import de.hglabor.plugins.uhc.player.PlayerList;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.utils.noriskutils.ChatUtils;
import de.hglabor.utils.noriskutils.TimeConverter;
import org.bukkit.*;

public class Border {
    private final int SHRINK_INTERVAL;
    private final int BORDER_SHRINK_SIZE;
    private final World overWorld;
    private int nextShrink;
    private int borderSize;
    private int nextBorderSize;
    private boolean cutInHalf;

    public Border() {
        this.nextShrink = UHCConfig.getInteger(CKeys.BORDER_FIRST_SHRINK);
        this.SHRINK_INTERVAL = UHCConfig.getInteger(CKeys.BORDER_SHRINK_INTERVAL);
        this.BORDER_SHRINK_SIZE = UHCConfig.getInteger(CKeys.BORDER_SHRINK_SIZE);
        this.borderSize = UHCConfig.getInteger(CKeys.BORDER_START_SIZE);
        this.overWorld = Bukkit.getWorld("world");
        init();
    }

    private void init() {
        overWorld.getWorldBorder().setDamageAmount(0);
        overWorld.getWorldBorder().setDamageBuffer(0);
        this.recalculateBorder();
    }

    private void createBorder(World world, int borderSize, int startPoint, int height) {
        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1)) {
            BlockVector3 negNeg = BukkitAdapter.asBlockVector(new Location(world, -borderSize, startPoint, -borderSize));
            BlockVector3 posPos = BukkitAdapter.asBlockVector(new Location(world, borderSize, startPoint + height, borderSize));
            CuboidRegion region = new CuboidRegion(weWorld, negNeg, posPos);
            editSession.setFastMode(true);
            editSession.makeCuboidWalls(region, BukkitAdapter.asBlockType(Material.BEDROCK));
        }
    }

    public void run() {
        if (borderSize > 25) {
            nextShrink += SHRINK_INTERVAL;
            borderSize = nextBorderSize;
            recalculateBorder();
            if (cutInHalf && borderSize <= 100) {
                createBorder(Bukkit.getWorld("world"), borderSize, 60, 10);
            } else {
                overWorld.getWorldBorder().setSize(borderSize * 2);
            }
            teleportToCorner();
        }
    }

    private void teleportToCorner() {
        for (UHCPlayer uhcPlayer : PlayerList.INSTANCE.getAlivePlayers()) {
            uhcPlayer.getBukkitPlayer().ifPresent(player -> {
                World world = borderSize > 500 ? player.getWorld() : Bukkit.getWorld("world");
                Location location = player.getLocation();
                Corner corner = Corner.getCorner(location);
                Outside outside = Outside.get(location, borderSize);
                int coord = borderSize - 5;
                int x, z;
                switch (outside) {
                    case X:
                        x = corner.xConverter.convert(coord);
                        player.teleportAsync(new Location(world, x, world.getHighestBlockYAt(x, location.getBlockZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES), location.getZ()));
                        break;
                    case Z:
                        z = corner.zConverter.convert(coord);
                        player.teleportAsync(new Location(world, location.getX(), world.getHighestBlockYAt(location.getBlockX(), z, HeightMap.MOTION_BLOCKING_NO_LEAVES), z));
                        break;
                    case BOTH:
                        x = corner.xConverter.convert(coord);
                        z = corner.zConverter.convert(coord);
                        player.teleportAsync(new Location(world, x, world.getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING_NO_LEAVES), z));
                        break;
                }
            });
        }
    }

    private void recalculateBorder() {
        if (borderSize > 500) {
            nextBorderSize = Math.max(borderSize - BORDER_SHRINK_SIZE, 400);
        } else if (borderSize > 25) {
            if (!cutInHalf) {
                cutInHalf = true;
                nextBorderSize = 400;
            }
            nextBorderSize = nextBorderSize / 2;
        }
    }

    public void announceBorderShrink(int time) {
        if (borderSize <= 25) return;
        int timeLeft = nextShrink - time;
        if (timeLeft <= 300) {
            if (timeLeft % 60 == 0 || timeLeft <= 5 || timeLeft == 10) {
                ChatUtils.broadcastMessage("border.shrink", ImmutableMap.of(
                        "size", String.valueOf(nextBorderSize),
                        "time", TimeConverter.stringify(timeLeft)));
            }
        }
    }

    public int getNextShrink() {
        return nextShrink;
    }

    public String getBorderString(int time) {
        return "Border: " + borderSize;
    }

    public int getBorderSize() {
        return borderSize;
    }
}
