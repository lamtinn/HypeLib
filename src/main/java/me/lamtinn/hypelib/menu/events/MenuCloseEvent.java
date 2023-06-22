package me.lamtinn.hypelib.menu.events;

import me.lamtinn.hypelib.menu.interfaces.HMenu;
import me.lamtinn.hypelib.menu.interfaces.event.HMenuEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MenuCloseEvent extends HMenuEvent {

    private final Player player;
    private final HMenu menu;

    public MenuCloseEvent(@NotNull Player player, @NotNull HMenu menu) {
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
}
