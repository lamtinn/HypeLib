package me.lamtinn.hypelib.utils;

import me.lamtinn.hypelib.object.PlayerObject;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class ItemUtils {

    public static void giveItem(final Player player, @NotNull ItemStack... itemStacks) {
        player.getInventory().addItem(itemStacks).values()
                .forEach(remaining -> player.getWorld().dropItem(player.getLocation(), remaining));
    }

    public static <T extends PlayerObject> void giveItem(final T player, @NotNull ItemStack... itemStacks) {
        if (player.getPlayer() != null) ItemUtils.giveItem(player.getPlayer(), itemStacks);
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

    public static ItemStack modify(@NotNull final ItemStack item, final Consumer<ItemStack> consumer) {
        consumer.accept(item);
        return new ItemStack(item);
    }

    public static ItemStack itemMetaModify(@NotNull final ItemStack item, final Consumer<ItemMeta> consumer) {
        return modify(item, i -> {
            final ItemMeta meta = i.getItemMeta();
            consumer.accept(meta);
            i.setItemMeta(meta);
        });
    }

    public static <IM extends ItemMeta> ItemStack metaModify(@NotNull final ItemStack item, final Consumer<IM> consumer) {
        return itemMetaModify(item, m -> consumer.accept((IM) m));
    }

    public static Map<Enchantment, Integer> convertEnchantments(@NotNull final List<String> values) {
        Map<Enchantment, Integer> maps = new HashMap<>();
        for (String e : values) {
            String[] parts = e.split(";", 1);
            Enchantment enchan = null;
            int level = 1;
            try {
                enchan = Enchantment.getByName(parts[0].toUpperCase());
            } catch (Exception ignored) {}
            if (parts.length > 1) level = Integer.parseInt(parts[1]);
            if (enchan != null) maps.put(enchan, level);
        }
        return maps;
    }

    public static List<ItemFlag> convertFlags(List<String> flags) {
        List<ItemFlag> list = new ArrayList<>();
        for (String f : flags) {
            ItemFlag itemFlag = null;
            try {
                itemFlag = ItemFlag.valueOf(f.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
            if (itemFlag != null) list.add(itemFlag);
        }
        return list;
    }
}
