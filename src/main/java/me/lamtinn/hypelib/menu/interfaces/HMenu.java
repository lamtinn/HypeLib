package me.lamtinn.hypelib.menu.interfaces;


import me.lamtinn.hypelib.menu.events.MenuClickEvent;
import me.lamtinn.hypelib.menu.events.MenuCloseEvent;
import me.lamtinn.hypelib.menu.events.MenuDragEvent;
import me.lamtinn.hypelib.menu.events.MenuOpenEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class HMenu {
    public abstract String title();

    public abstract int slots();

    public abstract MenuClick getClick();

    public abstract HMenu setClick(@NotNull HMenu.MenuClick action);

    public abstract boolean cancelAllClicks();

    public abstract boolean cooldown();

    public abstract HButton getButton(final int slot);

    public abstract void addButton(@NotNull HButton ... button);

    public abstract void addButton(@NotNull HButton button);

    public abstract HMenu onClick(final Consumer<MenuClickEvent> e);

    public abstract HMenu onOpen(final Consumer<MenuOpenEvent> e);

    public abstract HMenu onClose(final Consumer<MenuCloseEvent> e);

    public abstract HMenu onDrag(final Consumer<MenuDragEvent> e);

    public abstract void open(final Player player);

    public abstract void close();

    public enum MenuClick {
        TOP, BOTTOM, BOTH;
    }
}
