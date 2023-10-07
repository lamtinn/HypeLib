package me.lamtinn.hypelib.menu;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lamtinn.hypelib.menu.events.MenuClickEvent;
import me.lamtinn.hypelib.menu.events.MenuCloseEvent;
import me.lamtinn.hypelib.menu.events.MenuDragEvent;
import me.lamtinn.hypelib.menu.events.MenuOpenEvent;
import me.lamtinn.hypelib.menu.interfaces.HMenu;
import me.lamtinn.hypelib.menu.models.Button;
import me.lamtinn.hypelib.object.PlayerObject;
import me.lamtinn.hypelib.utils.AdventureUtils;
import me.lamtinn.hypelib.utils.ValueUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public abstract class Menu extends HMenu implements InventoryHolder {

    protected Inventory inventory;
    protected String title;
    protected int rows;
    protected MenuClick action;
    protected boolean cancelclicks;
    protected boolean cooldown;
    protected Map<Integer, Button> buttons;
    private Consumer<MenuClickEvent> click;
    private Consumer<MenuOpenEvent> open;
    private Consumer<MenuCloseEvent> close;
    private Consumer<MenuDragEvent> drag;
    private MenuUpdateTask updateTask;
    private Player player;

    public Menu(final String title, final int rows) {
        this.title = title;
        this.rows = rows * 9;
        this.action = MenuClick.BOTH;
        this.buttons = new ConcurrentHashMap<>();
        this.click = (menuClickEvent) -> {};
        this.open = (menuOpenEvent) -> {};
        this.close = (menuCloseEvent) -> {};
        this.drag = (menuDragEvent) -> {};
    }

    public Menu(final int rows) {
        this.title = "";
        this.rows = rows * 9;
        this.action = MenuClick.BOTH;
        this.buttons = new ConcurrentHashMap<>();
        this.click = (menuClickEvent) -> {};
        this.open = (menuOpenEvent) -> {};
        this.close = (menuCloseEvent) -> {};
        this.drag = (menuDragEvent) -> {};
    }

    public String title() {
        return this.title;
    }

    public void setTitle(@NotNull final String title) {
        this.title = title;
    }

    public int slots() {
        return this.rows;
    }

    public MenuClick getClick() {
        return this.action;
    }

    public HMenu setClick(@NotNull MenuClick action) {
        this.action = action;
        return this;
    }

    public boolean cancelAllClicks() {
        return this.cancelclicks;
    }

    public void setCancelClicks(final boolean cancelclicks) {
        this.cancelclicks = cancelclicks;
    }

    public boolean cooldown() {
        return this.cooldown;
    }

    public void setCooldown(final boolean cooldown) {
        this.cooldown = cooldown;
    }

    public Button getButton(int slot) {
        return this.buttons.get(slot);
    }

    public void addButton(@NotNull Button... button) {
        Arrays.stream(button).forEach(this::addButton);
    }

    public void addButton(@NotNull Button button) {
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

    public Menu onClick(final Consumer<MenuClickEvent> e) {
        this.click = e;
        return this;
    }

    public void getClick(final MenuClickEvent e) {
        this.click.accept(e);
    }

    public Menu onOpen(final Consumer<MenuOpenEvent> e) {
        this.open = e;
        return this;
    }

    public void getOpen(final MenuOpenEvent e) {
        this.open.accept(e);
    }

    public Menu onClose(final Consumer<MenuCloseEvent> e) {
        this.close = e;
        return this;
    }

    public void getClose(final MenuCloseEvent e) {
        this.close.accept(e);
    }

    public Menu onDrag(final Consumer<MenuDragEvent> e) {
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
        this.setButtons();
    }

    public void open(final Player player) {
        this.player = player;
        this.buttons.clear();

        String title = PlaceholderAPI.setPlaceholders(this.player, this.title());
        inventory = Bukkit.createInventory(this, slots(), AdventureUtils.toComponent(title));
        this.setButtons();

        final Player target = this.getPlayer();
        target.openInventory(this.inventory);

        this.updateTask = new MenuUpdateTask(this);
    }

    public <T extends PlayerObject> void open(final T player) {
        this.open(player.getPlayer());
    }

    public void close() {
        this.inventory.close();
        this.title = null;
        this.action = null;
        this.inventory = null;
        this.click = null;
        this.open = null;
        this.close = null;
        this.drag = null;

        if (this.updateTask != null) {
            this.updateTask.cancel();
        }
        this.updateTask = null;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public MenuUpdateTask getUpdateTask() {
        return this.updateTask;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Set<Button> getButtons() {
        return new HashSet<>(this.buttons.values());
    }

    public Map<Integer, Button> getItemMap() {
        return this.buttons;
    }

    public String parse(@NotNull final String value) {
        return PlaceholderAPI.setPlaceholders(this.player, value);
    }

    public int[] getSlots(final List<String> slots) {
        return slots.stream()
                .flatMapToInt(input -> {
                    if (input.contains("-") && input.split("-", 2).length == 2) {
                        String[] parts = input.split("-", 2);
                        int start = ValueUtils.isInteger(parts[0]);
                        int end = ValueUtils.isInteger(parts[1]);
                        if (start != -1 && end != -1 && start <= end && end <= this.slots()) {
                            return IntStream.rangeClosed(start, end);
                        }
                    } else {
                        int i = ValueUtils.isInteger(input);
                        if (i != -1 && i <= this.slots()) {
                            return IntStream.of(i);
                        }
                    }
                    return IntStream.empty();
                })
                .toArray();
    }

    public void setUpdateTask(final MenuUpdateTask updateTask) {
        this.updateTask = updateTask;
    }

    public void playerSound(@NotNull final String sound) {
        AdventureUtils.playerSound(this.player.getPlayer(), sound);
    }
}
