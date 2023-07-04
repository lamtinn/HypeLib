package me.lamtinn.hypelib.object;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PlayerObject {
    @Nullable Player getPlayer();

    @NotNull OfflinePlayer getOfflinePlayer();

    String getName();

    UUID getUUID();

    default @Nullable Location getLocation() {
        return this.getPlayer() == null ? null : this.getPlayer().getLocation();
    }

    default boolean isActive() {
        return (this.getPlayer() != null && this.getPlayer().isOnline());
    }

    default void doCommand(@NotNull String command) {
        if (this.getPlayer() != null) {
            this.getPlayer().performCommand(command);
        }
    }
}
