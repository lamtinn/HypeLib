package me.lamtinn.hypelib.command.subcommand;

import me.lamtinn.hypelib.command.annotation.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public abstract class SubCommand {

    private String permission, usage;

    public SubCommand(@NotNull final Permission permission, @NotNull final String usage) {
        this.permission = permission.getName();
        this.usage = usage;
    }

    public SubCommand(@NotNull final Permission permission) {
        this.permission = permission.getName();
        this.usage = "/none";
    }

    public SubCommand(@NotNull final String usage) {
        this.usage = usage;
        this.permission = "";
    }

    public abstract void onCommand(@NotNull final CommandContext context) throws IOException;

    public abstract List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final String[] args);

    public @Nullable Command getCommand() {
        return this.getClass().getAnnotation(Command.class);
    }

    public @NotNull String getPermission() {
        return this.permission;
    }

    public @NotNull String getUsage() {
        return this.usage;
    }

    public void setPermission(@NotNull final String permission) {
        this.permission = permission;
    }

    public void setPermission(@NotNull final Permission permission) {
        this.permission = permission.getName();
    }

    public void setUsage(@NotNull final String usage) {
        this.usage = usage;
    }
}
