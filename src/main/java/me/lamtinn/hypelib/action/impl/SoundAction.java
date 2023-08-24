package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.action.annotation.Identifiers;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Identifiers("sound")
public class SoundAction extends Action {

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        AdventureUtils.playerSound(player, executable);
    }
}
