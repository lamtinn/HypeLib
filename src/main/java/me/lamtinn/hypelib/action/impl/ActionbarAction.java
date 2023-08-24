package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.action.annotation.Identifiers;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Identifiers({"actionbar", "ab"})
public class ActionbarAction extends Action {

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        AdventureUtils.playerActionbar(player, executable);
    }
}
