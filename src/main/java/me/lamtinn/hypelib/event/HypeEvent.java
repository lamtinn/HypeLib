package me.lamtinn.hypelib.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public abstract class HypeEvent<E extends Event> implements Listener {

    @EventHandler
    public abstract void onEvent(E e);
}
