package me.lamtinn.hypelib.builder.item.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BaseheadGenerate extends ItemGenerate {

    @Override
    public String getType() {
        return "BASEHEAD";
    }

    @Override
    public ItemStack generate(@NotNull Player player, @NotNull ConfigurationSection section) {
        return this.generate(player, section.getString("base64", ""));
    }

    @Override
    public ItemStack generate(@NotNull Player player, @NotNull String value) {
        final String str = PlaceholderAPI.setPlaceholders(
                player, value
        );
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);

        ItemUtils.metaModify(itemStack, (SkullMeta meta) -> {
            GameProfile gameProfile = this.getGameProfile(str);
            Field field;

            try {
                field = Objects.requireNonNull(meta).getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(meta, gameProfile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
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

    private GameProfile getGameProfile(@NotNull final String base64) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", base64));
        return profile;
    }
}
