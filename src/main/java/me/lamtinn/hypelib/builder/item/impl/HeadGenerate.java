package me.lamtinn.hypelib.builder.item.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lamtinn.hypelib.builder.item.ItemBuilder;
import me.lamtinn.hypelib.builder.item.ItemGenerate;
import me.lamtinn.hypelib.utils.AdventureUtils;
import me.lamtinn.hypelib.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class HeadGenerate extends ItemGenerate {

    @Override
    public String getType() {
        return "head";
    }

    @Override
    public ItemStack generate(@NotNull Player player, @NotNull ConfigurationSection section) {
        final String value = PlaceholderAPI.setPlaceholders(
                player,
                section.getString("material").split("-")[1]
        );
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

        ItemUtils.metaModify(itemStack, (SkullMeta meta) ->
                meta.setOwner(value)
        );
        return itemStack;
    }

    @Override
    public ItemStack setMeta(@NotNull ItemBuilder builder) {
        builder.metaModify((SkullMeta meta) -> {
            meta.displayName(
                    AdventureUtils.toComponent(
                            builder.parse(builder.getName())
                    )
            );
            if (!builder.getLore().isEmpty()) {
                List<Component> lore = new ArrayList<>();
                builder.getLore().forEach(l -> lore.add(
                                AdventureUtils.toComponent(builder.parse(l))
                        )
                );
                meta.lore(lore);
            }
        });
        return builder.getItemStack();
    }
}
