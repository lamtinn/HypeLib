package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.action.annotation.Identifiers;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Identifiers({"player", "p"})
public class PlayerAction extends Action {

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        player.chat("/" + executable);
    }
}
