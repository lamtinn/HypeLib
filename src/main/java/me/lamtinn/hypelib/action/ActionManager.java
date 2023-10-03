package me.lamtinn.hypelib.action;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lamtinn.hypelib.action.annotation.Identifiers;
import me.lamtinn.hypelib.action.impl.*;
import me.lamtinn.hypelib.object.ReplacementPackage;
import me.lamtinn.hypelib.plugin.HypePlugin;
import me.lamtinn.hypelib.task.scheduler.Scheduler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionManager {

    private final HypePlugin plugin;
    private final Map<String, Action> actions;

    private String regex = "\\{(.+?)\\}";

    public ActionManager(@NotNull final HypePlugin plugin) {
        this.plugin = plugin;
        this.actions = new HashMap<>();

        register(PlayerAction::new);
        register(ConsoleAction::new);
        register(MessageAction::new);
        register(SoundAction::new);
        register(TitleAction::new);
        register(ChatAction::new);
        register(BossbarAction::new);
        register(BroadcastAction::new);
        register(ActionbarAction::new);
        register(CloseAction::new);
        register(RefreshAction::new);
    }

    public void setBrackets(final char first, final char second) {
        this.regex = "\\" + first + "(.+?)\\" + second;
    }

    public void run(@NotNull final Player player, @NotNull final String str, final ReplacementPackage replace) {
        if (!player.isOnline()) return;

        Action action = this.getAction(str);
        if (action == null) {
            return;
        }

        Scheduler.plugin(this.plugin).sync().runTask(() -> {
            String exe = PlaceholderAPI.setPlaceholders(player,
                    this.getExecutable(str)
            );
            if (replace != null) exe = replace.replace(exe);
            action.execute(player, exe);
        });
    }

    public void run(@NotNull final Player player, @NotNull final String str) {
        this.run(player, str, null);
    }

    public void run(@NotNull final Player player, @NotNull final List<String> actions, final ReplacementPackage replace) {
        actions.forEach(a -> this.run(player, a, replace));
    }

    public void run(@NotNull final Player player, @NotNull final List<String> actions) {
        actions.forEach(a -> this.run(player, a));
    }

    public void register(@NotNull final Action action) {
        final Identifiers identifiers = action.getClass()
                .getAnnotation(Identifiers.class);
        if (identifiers != null) {
            String[] value = identifiers.value();
            Arrays.stream(value).forEach(s -> this.actions.put(s, action));
        }
    }

    public void register(@NotNull final Supplier<Action> supplier) {
        this.register(supplier.get());
    }

    public void unregister(@NotNull final String identifier) {
        final Action action = this.actions.get(identifier);
        if (action == null) return;

        for (Map.Entry<String, Action> entry : this.actions.entrySet()) {
            if (entry.getValue() == action) {
                this.actions.remove(entry.getKey());
            }
        }
    }

    public void unregister(@NotNull final Action action) {
        final Identifiers identifiers = action.getClass().getAnnotation(Identifiers.class);
        for (final String identifier : identifiers.value()) {
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
                .filter(action -> {
                    final Identifiers identifiers = action.getClass()
                            .getAnnotation(Identifiers.class);
                    return Arrays.stream(identifiers.value())
                            .anyMatch(s -> s.startsWith(str) || s.equalsIgnoreCase(str)
                            );
                })
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
