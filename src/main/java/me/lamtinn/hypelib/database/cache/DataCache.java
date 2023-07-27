package me.lamtinn.hypelib.database.cache;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public interface DataCache<K, V> {

    void reload();

    void add(@NotNull final K value);

    boolean has(@NotNull final K value);

    V get(@NotNull final K value);

    @NotNull Collection<V> getAll();

    @NotNull Map<K, V> getMap();
}
