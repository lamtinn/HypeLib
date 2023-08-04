package me.lamtinn.hypelib.menu;

import me.lamtinn.hypelib.menu.models.Button;
import me.lamtinn.hypelib.plugin.HypePlugin;
import me.lamtinn.hypelib.task.Task;
import me.lamtinn.hypelib.utils.AdventureUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class MenuUpdateTask extends Task {

    private final Menu menu;

    public MenuUpdateTask(@NotNull final Menu menu) {
        super(HypePlugin.plugin, 2, true, true);
        this.menu = menu;
    }

    @Override
    public void onFinish() {
        Set<Button> buttons = menu.getButtons();
        if (buttons == null) return;

        buttons.forEach(button -> {
            if (button.getSlots() != null) {
                Arrays.stream(button.getSlots())
                        .mapToObj(i -> menu.getInventory().getItem(i))
                        .forEach(this::update);
            } else {
                ItemStack itemStack = menu.getInventory().getItem(button.getSlot());
                this.update(itemStack);
            }
        });
    }

    private void update(final ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        String name = AdventureUtils.toLegacy(meta.displayName());
        meta.displayName(AdventureUtils.toComponent(this.menu.parse(name)));

        List<Component> updatedLore = meta.hasLore() ? meta.lore().stream()
                .map(AdventureUtils::toLegacy)
                .map(this.menu::parse)
                .map(AdventureUtils::toComponent)
                .collect(Collectors.toList()) : null;

        meta.lore(updatedLore);
        itemStack.setItemMeta(meta);
    }
}
