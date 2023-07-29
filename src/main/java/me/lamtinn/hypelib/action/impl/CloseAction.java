package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class CloseAction extends Action {

    @Override
    public @NotNull Set<String> getIdentifiers() {
        return this.convert("close");
    }

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
        if (holder instanceof Menu) {
            player.closeInventory();
        }
    }
}
