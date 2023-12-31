package me.lamtinn.hypelib.builder.item.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lamtinn.hypelib.builder.item.ItemBuilder;
import me.lamtinn.hypelib.builder.item.ItemGenerate;
import me.lamtinn.hypelib.utils.AdventureUtils;
import me.lamtinn.hypelib.utils.ItemUtils;
import me.lamtinn.hypelib.utils.ValueUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
        return "HEAD";
    }

    @Override
    public ItemStack generate(@NotNull Player player, @NotNull ConfigurationSection section) {
        return this.generate(player, section.getString("head-owner",
                ValueUtils.getRandom(Bukkit.getOnlinePlayers()).getName()
        ));
    }

    @Override
    public ItemStack generate(@NotNull Player player, @NotNull String value) {
        final String str = PlaceholderAPI.setPlaceholders(
                player, value
        );
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

        ItemUtils.metaModify(itemStack, (SkullMeta meta) ->
                meta.setOwner(str)
        );
        return itemStack;
    }

    @Override
    public ItemBuilder setMeta(@NotNull ItemBuilder builder) {
        return builder.metaModify((SkullMeta meta) -> {
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
    }
}
