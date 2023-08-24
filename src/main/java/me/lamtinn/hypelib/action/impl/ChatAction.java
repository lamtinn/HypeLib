package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.action.annotation.Identifiers;
import me.lamtinn.hypelib.utils.AdventureUtils;
import me.lamtinn.hypelib.utils.PluginUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Identifiers("chat")
public class ChatAction extends Action {

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        Component component = AdventureUtils.toComponent(executable);
        player.chat(PluginUtils.color(AdventureUtils.toLegacy(component)));
    }
}
