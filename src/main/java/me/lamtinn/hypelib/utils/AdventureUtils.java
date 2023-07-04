package me.lamtinn.hypelib.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lamtinn.hypelib.plugin.HypePlugin;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class AdventureUtils {

    private static Supplier<String> prefix = () -> "<dark_gray>[<gradient:#a8ff78:#78ffd6>HypeLib</gradient><dark_gray>]";
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static String getPrefix() {
        return prefix.get();
    }

    public static void setPrefix(@NotNull final Supplier<String> prefix) {
        AdventureUtils.prefix = prefix;
    }

    public static void setPrefix(@NotNull final String prefix) {
        setPrefix(() -> prefix);
    }

    public static void sendMessage(CommandSender sender, String s) {
        if (sender instanceof Player) playerMessage((Player) sender, s);
        else consoleMessage(s);
    }

    public static void sendMessage(CommandSender sender, List<String> msgs) {
        msgs.forEach(msg -> sendMessage(sender, msg));
    }

    public static void sendMessage(CommandSender sender, String ... msgs) {
        Arrays.stream(msgs).forEach(msg -> sendMessage(sender, msg));
    }

    public static void consoleMessage(String message) {
        if (message == null || message.length() == 0) return;
        Audience au = HypePlugin.adventure.sender(Bukkit.getConsoleSender());
        Component parsed = mm.deserialize(message.replace("{prefix}", getPrefix()));
        au.sendMessage(parsed);
    }

    public static void consoleMessage(String[] messages) {
        for (String message : messages) {
            consoleMessage(message);
        }
    }

    public static void consoleMessage(List<String> messages) {
        for (String message : messages) {
            consoleMessage(message);
        }
    }

    public static void playerMessage(Player player, String message) {
        if (message == null || message.length() == 0) return;
        Audience au = HypePlugin.adventure.player(player);
        Component parsed = mm.deserialize(parse(player, message.replace("{prefix}", getPrefix())));
        au.sendMessage(parsed);
    }

    public static void playerMessage(Player player, String[] messages) {
        for (String message : messages) {
            playerMessage(player, message);
        }
    }

    public static void broadCastMessage(String message) {
        if (message == null || message.length() == 0) return;
        Audience au = HypePlugin.adventure.all();
        Component parsed = mm.deserialize(message.replace("{prefix}", getPrefix()));
        au.sendMessage(parsed);
    }

    public static void broadCastMessage(String[] messages) {
        for (String message : messages) {
            broadCastMessage(message);
        }
    }

    public static void playerSound(Player player, String soundPath) {
        Sound sound;
        float volume = 1.0f, pitch = 1.0f;

        if (!soundPath.contains(" ")) {
            try {
                sound = Sound.valueOf(soundPath.toUpperCase());
            } catch (IllegalArgumentException e) {
                consoleMessage("{prefix} <gray>Invalid sound's: <#FF416C>" + soundPath);
                return;
            }
        } else {
            String[] parts = soundPath.split(" ", 3);
            try {
                sound = Sound.valueOf(parts[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                consoleMessage("{prefix} <gray>Invalid sound's for action: <#FF416C>" + soundPath);
                return;
            }

            if (parts.length == 3) {
                try {
                    volume = Float.parseFloat(parts[1]);
                } catch (NumberFormatException e) {
                    consoleMessage("{prefix} <gray>Invalid volume for action: <#FF416C>" + soundPath);
                }

                try {
                    pitch = Float.parseFloat(parts[2]);
                } catch (NumberFormatException e) {
                    consoleMessage("{prefix} <gray>Invalid pitch for action: <#FF416C>" + soundPath);
                }
            }
        }
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void playerActionbar(Player player, String message) {
        if (message == null || message.length() == 0) return;
        Audience au = HypePlugin.adventure.player(player);
        Component parsed = mm.deserialize(parse(player, message.replace("{prefix}", getPrefix())));
        au.sendActionBar(parsed);
    }

    public static void playerTitle(Player player, ConfigurationSection config) {
        if (config.getString("text") == null) return;
        Audience au = HypePlugin.adventure.player(player);
        Title.Times times = Title.Times.times(
                Duration.ofMillis(config.getInt("delay.in") * 1000L),
                Duration.ofMillis(config.getInt("delay.time") * 1000L),
                Duration.ofMillis(config.getInt("delay.out") * 1000L)
        );

        String text = config.getString("text", "None"),
                subtext = config.getString("subtext", " ");

        Title parsedTitle = Title.title(
                mm.deserialize(parse(player, text)
                        .replace("{prefix}", getPrefix())
                ),
                mm.deserialize(parse(player, subtext)
                        .replace("{prefix}", getPrefix())
                ),
                times
        );
        au.showTitle(parsedTitle);
    }

    public static void playerTitle(Player player, String title, String sub, int in, int time, int out) {
        if (title == null) return;
        Audience au = HypePlugin.adventure.player(player);
        Title.Times times = Title.Times.times(
                Duration.ofMillis(in * 1000L),
                Duration.ofMillis(time * 1000L),
                Duration.ofMillis(out * 1000L)
        );

        Title parsedTitle = Title.title(
                mm.deserialize(parse(player, title)
                        .replace("{prefix}", getPrefix())
                ),
                mm.deserialize(parse(player, sub)
                        .replace("{prefix}", getPrefix())
                ),
                times
        );
        au.showTitle(parsedTitle);
    }

    public static void playerBossBar(Player player, String text, float progress, BossBar.Color color, BossBar.Overlay overlay) {
        if (text == null) return;
        text = parse(player, text.replace("{prefix}", getPrefix()));

        Audience au = HypePlugin.adventure.player(player);
        BossBar bossBar = BossBar.bossBar(mm.deserialize(text), progress, color, overlay);
        au.showBossBar(bossBar);

        Bukkit.getScheduler().runTaskLater(HypePlugin.plugin, () -> au.hideBossBar(bossBar), 3 * 20);
    }

    public static Component toComponent(String input) {
        return MINI_MESSAGE.deserialize("<reset><!italic>" + input);
    }

    public static String fromLegacy(String input) {
        Component component = LegacyComponentSerializer.legacySection().deserialize(input);
        return MINI_MESSAGE.serialize(component);
    }

    public static String toLegacy(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

    public static final MiniMessage MINI_MESSAGE = MiniMessage.builder().tags(
            StandardTags.defaults()
    ).build();

    public static String parse(final Player player, @NotNull String str) {
        return PlaceholderAPI.setPlaceholders(player, str);
    }
}
