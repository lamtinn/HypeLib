package me.lamtinn.hypelib.menu.events;

import me.lamtinn.hypelib.menu.interfaces.HButton;
import me.lamtinn.hypelib.menu.interfaces.event.HButtonEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ButtonClickEvent extends HButtonEvent {

    private final InventoryClickEvent event;
    private final HButton button;
    private final Player player;

    public ButtonClickEvent(final InventoryClickEvent event, final HButton button, final Player player) {
        this.event = event;
        this.button = button;
        this.player = player;
    }

    public ClickType getClickType() {
        return this.event.getClick();
    }

    @Override
    public HButton getButton() {
        return this.button;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }
}
