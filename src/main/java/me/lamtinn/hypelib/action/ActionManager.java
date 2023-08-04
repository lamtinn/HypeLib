package me.lamtinn.hypelib.action;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lamtinn.hypelib.action.impl.*;
import me.lamtinn.hypelib.plugin.HypePlugin;
import me.lamtinn.hypelib.task.scheduler.Scheduler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionManager {

    private final HypePlugin plugin;
    private final Map<String, Action> actions;

    private String regex = "\\{(.+?)\\}";

    public ActionManager(@NotNull final HypePlugin plugin) {
        this.plugin = plugin;
        this.actions = new HashMap<>();

        register(new PlayerAction());
        register(new ConsoleAction());
        register(new MessageAction());
        register(new SoundAction());
        register(new TitleAction());
        register(new ChatAction());
        register(new BossbarAction());
        register(new BroadcastAction());
        register(new ActionbarAction());
        register(new CloseAction());
    }

    public void setBrackets(final char first, final char second) {
        this.regex = "\\" + first + "(.+?)\\" + second;
    }

    public void run(@NotNull final Player player, @NotNull final String str) {
        if (!player.isOnline()) return;

        Action action = this.getAction(str);
        if (action == null) {
            return;
        }

        Scheduler.plugin(this.plugin).sync().runTask(() -> {
            String exe = PlaceholderAPI.setPlaceholders(player,
                    this.getExecutable(str)
            );
            action.execute(player, exe);
        });
    }

    public void run(@NotNull final Player player, @NotNull final List<String> actions) {
        actions.forEach(a -> this.run(player, a));
    }

    public void register(@NotNull final Action action) {
        action.getIdentifiers().forEach(s -> this.actions.put(s, action));
    }

    public void unregister(@NotNull final String identifier) {
        final Action action = this.actions.get(identifier);
        for (Map.Entry<String, Action> entry : this.actions.entrySet()) {
            if (entry.getValue() == action) {
                this.actions.remove(entry.getKey());
            }
        }
    }

    public void unregister(@NotNull final Action action) {
        for (final String identifier : action.getIdentifiers()) {
            this.actions.remove(identifier);
        }
    }

    public @NotNull String getExecutable(@NotNull final String str) {
        try {
            return str.trim().split(this.regex + " ", 2)[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }

    public @Nullable Action getAction(@NotNull String identifier) {
        Pattern pattern = Pattern.compile(this.regex);
        Matcher matcher = pattern.matcher(identifier);
        if (matcher.find()) {
            return this.get(matcher.group(1));
        }
        return null;
    }

    public @Nullable Action get(@NotNull final String str) {
        return this.getAll().stream()
                .filter(action -> action.getIdentifiers().stream().anyMatch(s -> s.startsWith(str) || s.equalsIgnoreCase(str)))
                .findFirst()
                .orElse(null);
    }

    public @NotNull Collection<Action> getAll() {
        return this.actions.values();
    }

    public @NotNull Map<String, Action> getActions() {
        return this.actions;
    }
}
