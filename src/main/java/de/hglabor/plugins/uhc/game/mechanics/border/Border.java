package de.hglabor.plugins.uhc.game.mechanics.border;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import de.hglabor.plugins.uhc.Uhc;
import de.hglabor.plugins.uhc.config.CKeys;
import de.hglabor.plugins.uhc.config.UHCConfig;
import de.hglabor.plugins.uhc.player.PlayerList;
import de.hglabor.plugins.uhc.player.UHCPlayer;
import de.hglabor.utils.noriskutils.TimeConverter;
import org.bukkit.*;
import org.bukkit.entity.Player;

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
        overWorld.getWorldBorder().setSize(borderSize * 2);
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
                // createBorder(Bukkit.getWorld("world"), borderSize, 60, 10);
            } else {
            }
            overWorld.getWorldBorder().setSize(borderSize * 2);
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
                        player.teleportAsync(new Location(world, x, world.getHighestBlockYAt(x, location.getBlockZ(), HeightMap.MOTION_BLOCKING_NO_LEAVES), location.getZ()).clone().add(0, 1, 0));
                        break;
                    case Z:
                        z = corner.zConverter.convert(coord);
                        player.teleportAsync(new Location(world, location.getX(), world.getHighestBlockYAt(location.getBlockX(), z, HeightMap.MOTION_BLOCKING_NO_LEAVES), z).clone().add(0, 1, 0));
                        break;
                    case BOTH:
                        x = corner.xConverter.convert(coord);
                        z = corner.zConverter.convert(coord);
                        player.teleportAsync(new Location(world, x, world.getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING_NO_LEAVES), z).clone().add(0, 1, 0));
                        break;
                }
            });
        }
    }

    private void borderPacket() {
        Bukkit.getScheduler().runTaskTimer(Uhc.getPlugin(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                isNearBorder(player, 10);
            }
        }, 0, 5);
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
              /*   ChatUtils.broadcastMessage("border.shrink", ImmutableMap.of(
                        "size", String.valueOf(nextBorderSize),
                        "time", TimeConverter.stringify(timeLeft)));*/
                Bukkit.broadcastMessage(ChatColor.AQUA + "Border will be shrinked to " +
                        ChatColor.RED + ChatColor.BOLD + nextBorderSize + "x" + nextBorderSize +ChatColor.RESET + ChatColor.AQUA +
                        " in " + ChatColor.GREEN + TimeConverter.stringify(timeLeft));
            }
        }
    }

    public int getNextShrink() {
        return nextShrink;
    }

    public String getBorderString(int time) {
        return ChatColor.AQUA + "Border: " + ChatColor.GREEN + borderSize;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public int getNextBorderSize() {
        return nextBorderSize;
    }

    private boolean isNearBorder(Player player, int puffer) {
        Location location = player.getLocation();
        if (borderSize - Math.abs(location.getBlockX()) < puffer) {
            Location toChange = new Location(overWorld, borderSize * convert(location.getX()), location.getBlockY(), location.getZ());
            int size = 2;
            for (int y = -size; y <= size; y++) {
                for (int z = -size; z <= size; z++) {
                    Location newState = toChange.clone().add(0, y, z);
                    sendBlockChange(player, newState);
                    Bukkit.getScheduler().runTaskLater(Uhc.getPlugin(), () -> removeBlockChange(player, newState, puffer), 2);
                }
            }
        }

        if (borderSize - Math.abs(location.getBlockZ()) < puffer) {
            Location toChange = new Location(overWorld, location.getX(), location.getBlockY(), borderSize * convert(location.getZ()));
            int size = 2;
            for (int y = -size; y <= size; y++) {
                for (int x = -size; x <= size; x++) {
                    Location newState = toChange.clone().add(x, y, 0);
                    sendBlockChange(player, newState);
                    Bukkit.getScheduler().runTaskLater(Uhc.getPlugin(), () -> removeBlockChange(player, newState, puffer), 2);
                }
            }
        }

        return true;
    }

    private void sendBlockChange(Player player, Location location) {
        if (location.getBlock().isSolid()) return;
        player.sendBlockChange(location, Material.RED_STAINED_GLASS.createBlockData());
    }

    private void removeBlockChange(Player player, Location location, int puffer) {
        double v = location.distanceSquared(player.getLocation());
        if (v > puffer * puffer) {
            player.sendBlockChange(location, location.getBlock().getBlockData());
        } else {
            Bukkit.getScheduler().runTaskLater(Uhc.getPlugin(), () -> removeBlockChange(player, location, puffer), 2);
        }
    }


    private int convert(double coord) {
        if (coord < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
