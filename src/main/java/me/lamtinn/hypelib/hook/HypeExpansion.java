package me.lamtinn.hypelib.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lamtinn.hypelib.plugin.HypePlugin;
import me.lamtinn.hypelib.utils.PluginUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Plugin("PlaceholderAPI")
public abstract class HypeExpansion extends PlaceholderExpansion implements Hook {

    protected final HypePlugin plugin;
    private final String identifier;

    public HypeExpansion(@NotNull final HypePlugin plugin, @NotNull final String identifier) {
        this.plugin = plugin;
        this.identifier = identifier;
    }

    public abstract String request(@NotNull final Player player, @NotNull String params);

    @Override
    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    @Override
    public @NotNull String getAuthor() {
        return "[lamtinn, LamTinn]";
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return null;
        return this.request(player, params.trim().toLowerCase());
    }

    @Override
    public void run() {
        print("Hooked onto PlaceholderAPI " + PluginUtils.getPluginVer("PlaceholderAPI"));
        this.register();
    }
}
