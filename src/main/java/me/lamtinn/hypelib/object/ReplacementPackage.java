package me.lamtinn.hypelib.object;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ReplacementPackage {

    private final HashMap<String, String> values;

    private String keyFormat = "${key}";

    public ReplacementPackage() {
        this.values = new HashMap<>();
    }

    public ReplacementPackage(@NotNull final HashMap<String, String> map) {
        this.values = map;
    }

    public ReplacementPackage setFormat(@NotNull final String keyFormat) {
        this.keyFormat = keyFormat;
        return this;
    }

    public ReplacementPackage add(@NotNull final String key, @NotNull final String value) {
        this.values.put(
                this.keyFormat.replace("key", key),
                value
        );
        return this;
    }

    public ReplacementPackage remove(@NotNull final String key) {
        this.values.remove(this.keyFormat.replace("key", key));
        return this;
    }

    public String replace(@NotNull final String string) {
        this.values.forEach(string::replace);
        return string;
    }

    public HashMap<String, String> getValues() {
        return this.values;
    }
}
