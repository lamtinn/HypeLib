package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.action.annotation.Identifiers;
import me.lamtinn.hypelib.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

@Identifiers("refresh")
public class RefreshAction extends Action {

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
        if (holder instanceof Menu menu) {
            menu.reloadButtons();
        }
    }
}
