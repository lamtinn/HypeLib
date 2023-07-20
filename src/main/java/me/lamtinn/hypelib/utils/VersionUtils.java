package me.lamtinn.hypelib.utils;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum VersionUtils {
    v1_8(47, "1.8.x"),
    v1_9(107, "1.9.x"),
    v1_10(210, "1.10.x"),
    v1_11(315, "1.11.x"),
    v1_12(335, "1.12.x"),
    v1_13(393, "1.13.x"),
    v1_14(477, "1.14.x"),
    v1_15(573, "1.15.x"),
    v1_16(735, "1.16.x"),
    v1_17(755, "1.17.x"),
    v1_18(757, "1.18.x"),
    v1_19(759, "1.19.x"),
    v1_20(763, "1.20.x");

    private final int id;
    private final String name;

    private static final String version;

    static {
        version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    VersionUtils(final int id, @NotNull final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static VersionUtils getVersion() {
        return Arrays.stream(values()).filter(versions -> version.startsWith(versions.name())).findFirst().orElse(null);
    }

    public static boolean isNewer(VersionUtils version) {
        return (getVersion().getId() > version.getId());
    }

    public static boolean isNewerOrSame(VersionUtils version) {
        return (getVersion().getId() >= version.getId());
    }

    public static boolean isLower(VersionUtils version) {
        return (getVersion().getId() < version.getId());
    }

    public static boolean isLowerOrSame(VersionUtils version) {
        return (getVersion().getId() <= version.getId());
    }
}
