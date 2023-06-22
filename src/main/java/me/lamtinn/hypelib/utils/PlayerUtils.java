package me.lamtinn.hypelib.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class PlayerUtils {

    @Nullable
    public static Player byName(@NotNull String name) {
        return Bukkit.getPlayer(name);
    }

    @Nullable
    public static Player byUUID(@NotNull UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    @NotNull
    public static List<Player> players() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    @NotNull
    public static List<UUID> uuids() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());
    }

    @NotNull
    public static List<String> names() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    public static int getPing(@NotNull final Player player) {
        Object entityPlayer;
        int ping = -99;
        try {
            entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
        } catch (Exception e) {
            return ping;
        }
        try {
            ping = entityPlayer.getClass().getField("ping").getInt(entityPlayer);
        } catch (Exception e) {
            try {
                ping = entityPlayer.getClass().getField("e").getInt(entityPlayer);
            } catch (Exception ignored) {
            }
        }
        return ping;
    }
}
