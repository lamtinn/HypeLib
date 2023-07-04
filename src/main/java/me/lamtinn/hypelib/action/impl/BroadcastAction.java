package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BroadcastAction extends Action {

    @Override
    public @NotNull Set<String> getIdentifiers() {
        return this.convert("{broadcast}", "{bc}");
    }

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        AdventureUtils.broadCastMessage(executable);
    }
}
