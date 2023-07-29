package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.utils.AdventureUtils;
import me.lamtinn.hypelib.utils.PluginUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ChatAction extends Action {

    @Override
    public @NotNull Set<String> getIdentifiers() {
        return this.convert("chat");
    }

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        Component component = AdventureUtils.toComponent(executable);
        player.chat(PluginUtils.color(AdventureUtils.toLegacy(component)));
    }
}
