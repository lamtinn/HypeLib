package me.lamtinn.hypelib.menu.events;

import me.lamtinn.hypelib.menu.Menu;
import me.lamtinn.hypelib.menu.interfaces.HButton;
import me.lamtinn.hypelib.menu.interfaces.HMenu;
import me.lamtinn.hypelib.menu.interfaces.event.HMenuEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class MenuClickEvent extends HMenuEvent implements Cancellable {

    private final InventoryClickEvent event;
    private final Player player;
    private final Menu menu;

    public MenuClickEvent(final InventoryClickEvent e, @NotNull Player player, @NotNull Menu menu) {
        this.event = e;
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

    public InventoryClickEvent getEvent() {
        return this.event;
    }

    public ClickType getClickType() {
        return this.event.getClick();
    }

    public InventoryAction getAction() {
        return this.event.getAction();
    }

    public InventoryView getView() {
        return this.event.getView();
    }

    public HButton getButton(final int index) {
        return this.menu.getButton(index);
    }

    public HButton getButton() {
        return getButton(this.event.getSlot());
    }

    public int getSlot() {
        return this.event.getSlot();
    }
}
