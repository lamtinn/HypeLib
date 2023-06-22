package me.lamtinn.hypelib.menu.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class HEvent extends Event {

    private static final HandlerList handlers;

    public abstract Player getPlayer();

    public @NotNull HandlerList getHandlers() {
        return HEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return HEvent.handlers;
    }

    static {
        handlers = new HandlerList();
    }
}
