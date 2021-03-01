package de.hglabor.plugins.uhc.game.scenarios;

import de.hglabor.plugins.uhc.game.Scenario;
import de.hglabor.utils.noriskutils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FlowerPower extends Scenario {
    public final static FlowerPower INSTANCE = new FlowerPower();

    private FlowerPower() {
        super("Flower Power", new ItemBuilder(Material.RED_TULIP).build());
    }

    private ArrayList<Material> flowers = new ArrayList<>(
            Arrays.asList(Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET,
                    Material.RED_TULIP, Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.OXEYE_DAISY,
                    Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.WITHER_ROSE, Material.SUNFLOWER, Material.LILAC,
                    Material.ROSE_BUSH, Material.PEONY));

    private List<Material> materials = Arrays.stream(Material.values())
            .filter(it -> !flowers.contains(it) &&
                    !it.name().contains("COMMAND") &&
                    !it.name().contains("POTTED") &&
                    !it.name().contains("_WALL_") &&
                    !it.name().contains("_GATEWAY") &&
                    !it.name().contains("PORTAL"))
            .collect(Collectors.toList());

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isEnabled()) {
            Block block = event.getBlock();
            if (flowers.contains(block.getType()) || block.getType().name().toLowerCase().contains("_coral")) {
                int i = 0;
                do {
                    try {
                        i = 0;
                        event.setDropItems(false);
                        Material material = getRandomMaterial();
                        ItemStack itemStack = new ItemStack(material);
                        if (itemStack.getMaxStackSize() > 1)
                            itemStack.setAmount(getRandomAmount());
                        Location location = block.getLocation();
                        location.getWorld().dropItem(location, itemStack);
                    } catch (IllegalArgumentException e) {
                        i++;
                    }
                } while (i > 0);
            }
        }
    }

    private Material getRandomMaterial() {
        int random = new Random().nextInt(materials.size());
        return materials.get(random);
    }

    private int getRandomAmount() {
        Random random = new Random();

        int r1 = random.nextInt(10);
        if (r1 < 3)
            return random.nextInt(10);
        else if (r1 < 6)
            return random.nextInt(15) + 10;
        else if (r1 < 8)
            return random.nextInt(20) + 25;
        else if (r1 < 9)
            return random.nextInt(19) + 45;
        else
            return random.nextInt(64);
    }
}