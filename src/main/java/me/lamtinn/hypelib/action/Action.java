package me.lamtinn.hypelib.action;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Action {

    public abstract void execute(@NotNull final Player player, @NotNull final String executable);
}
