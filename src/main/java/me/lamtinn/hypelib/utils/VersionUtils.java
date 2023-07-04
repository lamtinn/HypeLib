package me.lamtinn.hypelib.utils;

import org.bukkit.Bukkit;

import java.util.Arrays;

public enum VersionUtils {
    v1_8(47),
    v1_9(107),
    v1_10(210),
    v1_11(315),
    v1_12(335),
    v1_13(393),
    v1_14(477),
    v1_15(573),
    v1_16(735),
    v1_17(755),
    v1_18(757),
    v1_19(759);

    private final int id;

    private static final String version;

    static {
        version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    VersionUtils(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
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
