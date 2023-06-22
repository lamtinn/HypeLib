package me.lamtinn.hypelib.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemUtils {

    public static void giveItem(final Player player, @NotNull ItemStack... itemStacks) {
        player.getInventory().addItem(itemStacks).values()
                .forEach(remaining -> player.getWorld().dropItem(player.getLocation(), remaining));
    }

    public static void removeItem(final Inventory inv, @NotNull Collection<ItemStack> items) {
        for (ItemStack item : items) {
            inv.removeItem(item);
        }
    }

    public static Material randonMaterial() {
        List<Material> materials = Arrays.asList(Material.values());
        int randomIndex = ThreadLocalRandom.current().nextInt(materials.size());
        return materials.get(randomIndex);
    }
}
