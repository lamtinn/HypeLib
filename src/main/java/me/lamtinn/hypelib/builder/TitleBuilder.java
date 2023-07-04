package me.lamtinn.hypelib.builder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lamtinn.hypelib.plugin.HypePlugin;
import me.lamtinn.hypelib.utils.AdventureUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TitleBuilder {

    private Player player;

    private String title, subtitle;

    private int in, time, out;

    public TitleBuilder() {
    }

    public TitleBuilder(Player player) {
        this.player = player;
    }

    public TitleBuilder(@NotNull ConfigurationSection section) {
        this.title = PlaceholderAPI.setPlaceholders(this.player, section.getString("text", "<white>None"));
        this.subtitle = PlaceholderAPI.setPlaceholders(this.player, section.getString("sub-text", "<white>Null title..."));

        this.in = section.getInt("delay.in", 2);
        this.time = section.getInt("delay.time", 3);
        this.out = section.getInt("delay.out", 2);
    }

    public void send() {
        if (player == null || !player.isOnline()) return;
        AdventureUtils.playerTitle(this.player, this.title, this.subtitle, this.in, this.time, this.out);
    }

    public void clear() {
        HypePlugin.adventure.player(player).clearTitle();
    }

    public TitleBuilder setPlayer(@NotNull Player player) {
        this.player = player;
        return this;
    }

    public TitleBuilder setTitle(@NotNull String title) {
        this.title = title;
        return this;
    }

    public TitleBuilder setSubtitle(@NotNull String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public TitleBuilder setIn(final int in) {
        this.in = in;
        return this;
    }

    public TitleBuilder setOut(final int out) {
        this.out = out;
        return this;
    }

    public TitleBuilder setTime(final int time) {
        this.time = time;
        return this;
    }

    public TitleBuilder replace(@NotNull String key, @NotNull Object value) {
        title = title.replace("{" + key + "}", String.valueOf(value));
        subtitle = subtitle.replace("{" + key + "}", String.valueOf(value));
        return this;
    }
}
