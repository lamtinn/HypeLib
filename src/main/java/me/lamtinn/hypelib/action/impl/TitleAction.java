package me.lamtinn.hypelib.action.impl;

import me.lamtinn.hypelib.action.Action;
import me.lamtinn.hypelib.action.annotation.Identifiers;
import me.lamtinn.hypelib.builder.TitleBuilder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Identifiers("title")
public class TitleAction extends Action {

    @Override
    public void execute(@NotNull Player player, @NotNull String executable) {
        String text = "None", subtext = " ";
        int in = 1, time = 2, out = 1;

        TitleBuilder title = new TitleBuilder(player);
        if (!executable.contains(" ")) {
            text = executable;
        } else {
            String[] parts = executable.split(" ", 5);
            if (parts.length == 5) {
                text = parts[0];
                subtext = parts[1];

                try {
                    in = Integer.parseInt(parts[2]);
                } catch (NumberFormatException ignored) {
                }

                try {
                    time = Integer.parseInt(parts[3]);
                } catch (NumberFormatException ignored) {
                }

                try {
                    out = Integer.parseInt(parts[4]);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        title.setTitle(text).setSubtitle(subtext).setIn(in).setTime(time).setOut(out);
        title.send();
    }
}
