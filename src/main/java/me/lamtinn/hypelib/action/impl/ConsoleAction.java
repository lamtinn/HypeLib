package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.action.annotation.Identifiers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Identifiers("console")
public class ConsoleAction extends Action {

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executable);
    }
}
