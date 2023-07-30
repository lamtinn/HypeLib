package me.lamtinn.hypelib.builder.item;

import me.lamtinn.hypelib.utils.AdventureUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemGenerate {

    public abstract String getType();

    public abstract ItemStack generate(@NotNull final Player player, @NotNull final ConfigurationSection section);

    public ItemStack setMeta(@NotNull final ItemBuilder builder) {
        builder.metaModify(meta -> {
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
