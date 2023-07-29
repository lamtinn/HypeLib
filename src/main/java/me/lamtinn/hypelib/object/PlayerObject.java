package me.lamtinn.hypelib.object;

import me.lamtinn.hypelib.plugin.HypePlugin;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

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

    default PlayerObject modifyData(final Consumer<PersistentDataContainer> consumer) {
        consumer.accept(this.getPlayer().getPersistentDataContainer());
        return this;
    }

    default <T, Z> PlayerObject setData(@NotNull String key, final PersistentDataType<T, Z> type, final Z value) {
        return this.modifyData(d -> d.set(HypePlugin.plugin.key(key), type, value));
    }

    default PlayerObject setData(@NotNull String key, final byte value) {
        return this.setData(key, PersistentDataType.BYTE, value);
    }

    default PlayerObject setData(@NotNull String key, final byte[] values) {
        return this.setData(key, PersistentDataType.BYTE_ARRAY, values);
    }

    default PlayerObject setData(@NotNull String key, final double value) {
        return this.setData(key, PersistentDataType.DOUBLE, value);
    }

    default PlayerObject setData(@NotNull String key, final float value) {
        return this.setData(key, PersistentDataType.FLOAT, value);
    }

    default PlayerObject setData(@NotNull String key, final int value) {
        return this.setData(key, PersistentDataType.INTEGER, value);
    }

    default PlayerObject setData(@NotNull String key, final int[] values) {
        return this.setData(key, PersistentDataType.INTEGER_ARRAY, values);
    }

    default PlayerObject setData(@NotNull String key, final long value) {
        return this.setData(key, PersistentDataType.LONG, value);
    }

    default PlayerObject setData(@NotNull String key, final long[] values) {
        return this.setData(key, PersistentDataType.LONG_ARRAY, values);
    }

    default PlayerObject setData(@NotNull String key, final short value) {
        return this.setData(key, PersistentDataType.SHORT, value);
    }

    default PlayerObject setData(@NotNull String key, final String value) {
        return this.setData(key, PersistentDataType.STRING, value);
    }

    default PlayerObject setData(@NotNull String key, final PersistentDataContainer value) {
        return this.setData(key, PersistentDataType.TAG_CONTAINER, value);
    }

    default PlayerObject setData(@NotNull String key, final PersistentDataContainer[] values) {
        return this.setData(key, PersistentDataType.TAG_CONTAINER_ARRAY, values);
    }

    default PlayerObject removeData(@NotNull String key) {
        return this.modifyData(d -> d.remove(HypePlugin.plugin.key(key)));
    }

    default <T, Z> Z getData(@NotNull String key, PersistentDataType<T, Z> type) {
        return this.getPlayer()
                .getPersistentDataContainer()
                .get(HypePlugin.plugin.key(key), type);
    }

    default boolean hasData(@NotNull String key, PersistentDataType type) {
        return this.getPlayer()
                .getPersistentDataContainer()
                .has(HypePlugin.plugin.key(key), type);
    }
}
