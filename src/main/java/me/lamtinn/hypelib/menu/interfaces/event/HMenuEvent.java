package me.lamtinn.hypelib.menu.interfaces.event;

import me.lamtinn.hypelib.menu.interfaces.HMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class HMenuEvent extends Event {

    private static final HandlerList handlers;

    public abstract HMenu getMenu();

    public abstract Player getPlayer();

    public @NotNull HandlerList getHandlers() {
        return HMenuEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return HMenuEvent.handlers;
    }

    static {
        handlers = new HandlerList();
    }
}
