package me.lamtinn.hypelib.builder;

import me.lamtinn.hypelib.utils.AdventureUtils;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BossbarBuilder {

    private Player player;

    private String text;

    private float progress;

    private BossBar.Color color;

    private BossBar.Overlay overlay;

    public BossbarBuilder() {
    }

    public BossbarBuilder(@NotNull Player player) {
        this.player = player;
    }

    public void send() {
        if (player == null || !player.isOnline()) return;
        AdventureUtils.playerBossBar(player, text, progress, color, overlay);
    }

    public BossbarBuilder setPlayer(@NotNull Player player) {
        this.player = player;
        return this;
    }

    public BossbarBuilder setText(@NotNull String text) {
        this.text = text;
        return this;
    }

    public BossbarBuilder setProgress(final float progress) {
        this.progress = progress;
        return this;
    }

    public BossbarBuilder setColor(@NotNull BossBar.Color color) {
        this.color = color;
        return this;
    }

    public BossbarBuilder setOverlay(@NotNull BossBar.Overlay overlay) {
        this.overlay = overlay;
        return this;
    }

    public BossbarBuilder replace(@NotNull String key, @NotNull Object value) {
        text = text.replace("{" + key + "}", String.valueOf(value));
        return this;
    }
}
