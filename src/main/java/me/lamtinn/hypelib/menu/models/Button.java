package me.lamtinn.hypelib.menu.models;

import me.lamtinn.hypelib.menu.events.ButtonClickEvent;
import me.lamtinn.hypelib.menu.interfaces.HButton;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class Button implements HButton {

    private final ItemStack icon;

    private String key, value;

    private int slot;
    private int[] slots;

    private Consumer<ButtonClickEvent> click;

    public Button(final ItemStack itemStack) {
        this.icon = itemStack;
        this.click = (buttonClickEvent -> {});
    }

    @Override
    public @NotNull String getKey() {
        return this.key;
    }

    @Override
    public void setKey(@NotNull String key) {
        this.key = key;
    }

    @Override
    public @NotNull String getValue() {
        return this.value;
    }

    @Override
    public void setValue(@NotNull String value) {
        this.value = value;
    }

    @Override
    public ItemStack getIcon() {
        return this.icon;
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public HButton setSlot(final int slot) {
        this.slot = slot;
        return this;
    }

    @Override
    public int[] getSlots() {
        return this.slots;
    }

    @Override
    public HButton setSlots(final int... slots) {
        this.slots = slots;
        return this;
    }

    @Override
    public HButton onClick(final Consumer<ButtonClickEvent> e) {
        this.click = e;
        return this;
    }

    @Override
    public void setClick(final ButtonClickEvent e) {
        this.click.accept(e);
    }

    @Override
    public String toString() {
        return "key=" + this.key + "value=" + this.value + "type=" + this.icon.getType();
    }
}
