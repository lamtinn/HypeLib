package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.builder.BossbarBuilder;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BossbarAction extends Action {

    @Override
    public @NotNull Set<String> getIdentifiers() {
        return this.convert("{bossbar}", "{bb}");
    }

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        String str = "None";
        float progress = 1;
        BossBar.Color color = BossBar.Color.RED;
        BossBar.Overlay overlay = BossBar.Overlay.PROGRESS;

        BossbarBuilder builder = new BossbarBuilder(player);
        if (!executable.contains(" ")) {
            str = executable;
        } else {
            String[] parts = executable.split(" ", 4);
            if (parts.length == 4) {
                str = parts[0];

                try {
                    progress = Float.parseFloat(parts[1]);
                } catch (NumberFormatException ignored) {
                }

                try {
                    color = BossBar.Color.valueOf(parts[2].toUpperCase());
                } catch (IllegalArgumentException ignored) {
                }

                try {
                    overlay = BossBar.Overlay.valueOf(parts[3].toUpperCase());
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        builder.setText(str).setProgress(progress).setColor(color).setOverlay(overlay);
        builder.send();
    }
}
