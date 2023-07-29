package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PlayerAction extends Action {

    @Override
    public @NotNull Set<String> getIdentifiers() {
        return this.convert("player", "p");
    }

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        player.chat("/" + executable);
    }
}
