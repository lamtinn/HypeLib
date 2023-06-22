package me.lamtinn.hypelib.menu.events;

import me.lamtinn.hypelib.menu.interfaces.HMenu;
import me.lamtinn.hypelib.menu.interfaces.event.HMenuEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

public class MenuDragEvent extends HMenuEvent implements Cancellable {

    private final InventoryDragEvent event;
    private final Player player;
    private final HMenu menu;

    public MenuDragEvent(final InventoryDragEvent event, @NotNull Player player, @NotNull HMenu menu) {
        this.event = event;
        this.player = player;
        this.menu = menu;
    }

    @Override
    public HMenu getMenu() {
        return this.menu;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean isCancelled() {
        return this.event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.event.setCancelled(cancel);
    }

    public InventoryDragEvent getEvent() {
        return this.event;
    }
}
