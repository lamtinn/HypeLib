package me.lamtinn.hypelib.menu;

import me.lamtinn.hypelib.menu.models.Button;
import me.lamtinn.hypelib.plugin.HypePlugin;
import me.lamtinn.hypelib.task.Task;
import me.lamtinn.hypelib.task.scheduler.Scheduler;
import me.lamtinn.hypelib.utils.PluginUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MenuUpdateTask extends Task {

    private final Menu menu;

    public MenuUpdateTask(@NotNull final Menu menu) {
        super(HypePlugin.plugin, 1, true, true);
        this.menu = menu;
    }

    @Override
    public void onFinish() {
        Scheduler.plugin(plugin)
                .sync()
                .runTask(this.menu::reloadButtons);

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

    @Override
    public void onRun() {
    }

    @SuppressWarnings("all")
    private void update(final ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        meta.setDisplayName(PluginUtils.color(
                menu.parse(meta.getDisplayName())
        ));

        List<String> updatedLore = meta.hasLore() ? meta.getLore().stream()
                .map(PluginUtils::color)
                .map(this.menu::parse)
                .collect(Collectors.toList()) : null;

        meta.setLore(updatedLore);
        itemStack.setItemMeta(meta);
    }
}
