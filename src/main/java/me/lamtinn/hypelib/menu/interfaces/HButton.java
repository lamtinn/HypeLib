package me.lamtinn.hypelib.menu.interfaces;

import me.lamtinn.hypelib.menu.events.ButtonClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface HButton {
    @NotNull String getKey();

    void setKey(final String key);

    @NotNull String getValue();

    void setValue(final String value);

    ItemStack getIcon();

    int getSlot();

    HButton setSlot(final int slot);

    int[] getSlots();

    HButton setSlots(final int ... slots);

    HButton onClick(final Consumer<ButtonClickEvent> e);

    void setClick(final ButtonClickEvent e);
}
