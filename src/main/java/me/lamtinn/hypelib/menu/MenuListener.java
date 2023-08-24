package me.lamtinn.hypelib.menu;

import me.lamtinn.hypelib.menu.events.*;
import me.lamtinn.hypelib.menu.interfaces.HButton;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MenuListener implements Listener {

    private final Map<String, Double> cooldowns = new HashMap<>();

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof Menu menu)) return;

        if (!isCorrectMenu(e, menu)) return;

        e.setCancelled(menu.cancelAllClicks());
        if (menu.cooldown() && isCooldown(player.getName())) return;

        HButton button = menu.getButton(e.getSlot());
        if (button == null) return;

        switch (menu.getClick()) {
            case TOP -> {
                if (!Objects.equals(e.getClickedInventory(), e.getView().getTopInventory())) return;
            }
            case BOTTOM -> {
                if (!Objects.equals(e.getClickedInventory(), e.getView().getBottomInventory())) return;
            }
        }

        menu.getClick(new MenuClickEvent(e, player, menu));
        button.setClick(new ButtonClickEvent(e, button, player));

        player.updateInventory();
        if (!menu.getItemMap().isEmpty()) {
            menu.reloadButtons();
        }
    }

    @EventHandler
    public void onInvDrag(InventoryDragEvent e) {
        Player player = (Player) e.getWhoClicked();
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof Menu menu)) return;

        if (!isCorrectMenu(e, menu)) return;

        menu.getDrag(new MenuDragEvent(e, player, menu));
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof Menu menu)) return;

        if (!isCorrectMenu(e, menu)) return;

        menu.getOpen(new MenuOpenEvent(player, menu));
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        InventoryHolder holder = e.getInventory().getHolder();
        if (!(holder instanceof Menu menu)) return;

        if (!isCorrectMenu(e, menu)) return;

        menu.getClose(new MenuCloseEvent(player, menu));

        if (menu.getUpdateTask() != null) {
            menu.getUpdateTask().cancel();
            menu.setUpdateTask(null);
        }
    }

    private boolean isCorrectMenu(InventoryEvent e, Menu menu) {
        return e.getView().title().equals(AdventureUtils.toComponent(menu.title()));
    }

    private boolean isCooldown(final String name) {
        final double current = System.currentTimeMillis() / 1000.0;
        final Double last = this.cooldowns.get(name);
        if (last != null) {
            final double time = last;
            if (current < time) {
                return true;
            }
        }
        this.cooldowns.put(name, current + 0.35);
        return false;
    }
}
