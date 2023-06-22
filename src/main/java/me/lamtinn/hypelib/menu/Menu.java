package me.lamtinn.hypelib.menu;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lamtinn.hypelib.menu.events.MenuClickEvent;
import me.lamtinn.hypelib.menu.events.MenuCloseEvent;
import me.lamtinn.hypelib.menu.events.MenuDragEvent;
import me.lamtinn.hypelib.menu.events.MenuOpenEvent;
import me.lamtinn.hypelib.menu.interfaces.HButton;
import me.lamtinn.hypelib.menu.interfaces.HMenu;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class Menu extends HMenu implements InventoryHolder {

    protected Inventory inventory;

    protected String title;
    protected int rows;
    protected ClickAction action;
    protected boolean cancelclicks;
    protected boolean cooldown;

    protected Map<Integer, HButton> buttons;

    private Consumer<MenuClickEvent> click;
    private Consumer<MenuOpenEvent> open;
    private Consumer<MenuCloseEvent> close;
    private Consumer<MenuDragEvent> drag;

    private Player player;

    public Menu(final String title, final int rows) {
        this.title = title;
        this.rows = rows * 9;
        this.action = ClickAction.BOTH;
        this.buttons = new ConcurrentHashMap<>();
        this.click = (menuClickEvent -> {});
        this.open = (menuOpenEvent -> {});
        this.close = (menuCloseEvent -> {});
        this.drag = (InventoryDragEvent -> {});
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public int slots() {
        return this.rows;
    }

    @Override
    public ClickAction getClickAction() {
        return this.action;
    }

    @Override
    public HMenu setClickAction(@NotNull HMenu.ClickAction action) {
        this.action = action;
        return this;
    }

    @Override
    public boolean cancelAllClicks() {
        return this.cancelclicks;
    }

    public void setCancelClicks(final boolean cancelclicks) {
        this.cancelclicks = cancelclicks;
    }

    @Override
    public boolean cooldown() {
        return this.cooldown;
    }

    public void setCooldown(final boolean cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public HButton getButton(int slot) {
        if (!this.buttons.containsKey(slot)) return null;
        return this.buttons.get(slot);
    }

    @Override
    public void addButton(@NotNull HButton @NotNull ... button) {
        Arrays.stream(button).forEach(this::addButton);
    }

    @Override
    public void addButton(@NotNull HButton button) {
        if (button.getSlots() != null) {
            for (int slot : button.getSlots()) {
                if (slot == -1 || slot > this.slots() || this.buttons.containsKey(slot)) {
                    continue;
                }
                this.buttons.put(slot, button);
                this.inventory.setItem(slot, button.getIcon());
            }
        } else {
            int i = button.getSlot();
            if (i == -1 || i > this.slots() || this.buttons.containsKey(i)) {
                return;
            }
            this.buttons.put(i, button);
            this.inventory.setItem(i, button.getIcon());
        }
    }

    @Override
    public HMenu onClick(final Consumer<MenuClickEvent> e) {
        this.click = e;
        return this;
    }

    public void getClick(final MenuClickEvent e) {
        this.click.accept(e);
    }

    @Override
    public HMenu onOpen(final Consumer<MenuOpenEvent> e) {
        this.open = e;
        return this;
    }

    public void getOpen(final MenuOpenEvent e) {
        this.open.accept(e);
    }

    @Override
    public HMenu onClose(final Consumer<MenuCloseEvent> e) {
        this.close = e;
        return this;
    }

    public void getClose(final MenuCloseEvent e) {
        this.close.accept(e);
    }

    @Override
    public HMenu onDrag(final Consumer<MenuDragEvent> e) {
        this.drag = e;
        return this;
    }

    public void getDrag(final MenuDragEvent e) {
        this.drag.accept(e);
    }

    public abstract void setButtons();

    public void reloadButtons() {
        this.buttons.clear();
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, null);
        }
        setButtons();
    }

    @Override
    public void open(final Player player) {
        this.player = player;
        this.buttons.clear();

        String title = PlaceholderAPI.setPlaceholders(this.player, this.title());
        inventory = Bukkit.createInventory(this, slots(), AdventureUtils.toComponent(title));
        this.setButtons();

        final Player target = this.getPlayer();
        target.openInventory(this.inventory);
    }

    @Override
    public void close() {
        this.inventory.close();
        this.title = null;
        this.action = null;
        this.inventory = null;
        this.click = null;
        this.open = null;
        this.close = null;
        this.drag = null;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public @NotNull Player getPlayer() {
        return this.player;
    }

    public void playerSound(String sound) {
        AdventureUtils.playerSound(this.player.getPlayer(), sound);
    }
}
