package me.lamtinn.hypelib.menu.interfaces.event;

import me.lamtinn.hypelib.menu.interfaces.HButton;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class HButtonEvent extends Event {

    private static final HandlerList handlers;

    public abstract HButton getButton();

    public abstract Player getPlayer();

    public @NotNull HandlerList getHandlers() {
        return HButtonEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return HButtonEvent.handlers;
    }

    static {
        handlers = new HandlerList();
    }
}
