package me.lamtinn.hypelib.menu;

import me.lamtinn.hypelib.menu.events.*;
import me.lamtinn.hypelib.menu.interfaces.HButton;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class MenuListener implements Listener {

    private final Map<String, Double> cooldowns = new HashMap<>();

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (e.getClickedInventory() == null) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        final InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            if (e.getView().title().equals(AdventureUtils.toComponent(menu.title()))) {
                if (menu.cancelAllClicks()) {
                    e.setCancelled(true);
                }
                if (menu.cooldown()) {
                    if (isCooldown(player.getName())) {
                        return;
                    }
                }

                final HButton button = menu.getButton(e.getSlot());
                if (button == null) {
                    return;
                }

                switch (menu.getClickAction()) {
                    case TOP:
                        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) {
                            return;
                        }
                        break;
                    case BOTTOM:
                        if (!e.getClickedInventory().equals(e.getView().getBottomInventory())) {
                            return;
                        }
                        break;
                }
                menu.getClick(new MenuClickEvent(e, player, (Menu) holder));
                button.setClick(new ButtonClickEvent(e, button, player));
            }
        }
    }

    @EventHandler
    public void onInvDrag(InventoryDragEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        final InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            if (e.getView().title().equals(AdventureUtils.toComponent(menu.title()))) {
                menu.getDrag(new MenuDragEvent(e, player, menu));
            }
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;

        Player player = (Player) e.getPlayer();
        final InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            if (e.getView().title().equals(AdventureUtils.toComponent(menu.title()))) {
                menu.getOpen(new MenuOpenEvent(player, menu));
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;

        Player player = (Player) e.getPlayer();
        final InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Menu) {
            Menu menu = (Menu) holder;
            if (e.getView().title().equals(AdventureUtils.toComponent(menu.title()))) {
                menu.getClose(new MenuCloseEvent(player, menu));
            }
        }
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
