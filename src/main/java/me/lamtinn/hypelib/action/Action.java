package me.lamtinn.hypelib.action;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Action {

    @NotNull
    public abstract Set<String> getIdentifiers();

    public abstract void execute(@NotNull final Player player, @NotNull final String executable);

    public @NotNull Set<String> convert(@NotNull final String ... identifiers) {
        return new HashSet<>(Arrays.asList(identifiers));
    }
}
