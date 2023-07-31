package me.lamtinn.hypelib.builder.item;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lamtinn.hypelib.plugin.HypePlugin;
import me.lamtinn.hypelib.utils.AdventureUtils;
import me.lamtinn.hypelib.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemBuilder {

    private static final Map<String, ItemGenerate> materials = new HashMap<>();

    public static void register(@NotNull final ItemGenerate itemGenerate) {
        ItemBuilder.materials.put(
                itemGenerate.getType().toLowerCase(),
                itemGenerate
        );
    }

    private final Player player;

    private final ItemGenerate generate;
    private final ItemStack itemStack;

    private String name = " ";
    private List<String> lore = Collections.emptyList();

    public ItemBuilder(@NotNull final Player player, @NotNull final ConfigurationSection section) {
        this.player = player;
        this.generate = ItemBuilder.materials.getOrDefault(
                section.getString("material", "DIRT").toLowerCase(),
                null
        );

        if (generate != null) {
            this.itemStack = generate.generate(this.player, section);
        } else {
            Material mate = Material.getMaterial(
                    section.getString("material", "DIRT").toUpperCase()
            );
            this.itemStack = new ItemStack(mate == null ? Material.DIRT : mate);
        }

        if (section.contains("name") && section.isString("name")) {
            this.name = section.getString("name");
        }
        if (section.contains("amount") && section.isInt("amount")) {
            this.setAmount(section.getInt("amount"));
        }
        if (section.contains("model-data") && section.isInt("model-data")) {
            this.setCustomModel(section.getInt("model-data"));
        }
        if (section.contains("lore")) {
            if (section.isList("lore")) {
                this.setLores(section.getStringList("lore"));
            }
        }
        if (section.contains("enchantments") && section.isList("enchantments")) {
            Map<Enchantment, Integer> enchs = ItemUtils.convertEnchantments(section.getStringList("enchantments"));
            this.setEnchantments(enchs);
        }
        if (section.contains("flags") && section.isList("flags")) {
            List<ItemFlag> flags = ItemUtils.convertFlags(section.getStringList("flags"));
            this.setFlags(flags);
        }
    }

    public ItemBuilder setName(@NotNull final String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder setMaterial(@NotNull final Material material) {
        return this.modify(i -> i.setType(material));
    }

    public ItemBuilder setMaterial(@NotNull final String value) {
        Material material = Material.getMaterial(value.toUpperCase());
        return this.setMaterial(material == null ? Material.DIRT : material);
    }

    public ItemBuilder setAmount(final int amount) {
        return this.modify(i -> i.setAmount(amount));
    }

    public ItemBuilder addAmount(final int amount) {
        return this.modify(i -> i.setAmount(i.getAmount() + amount));
    }

    public ItemBuilder subtractAmount(final int amount) {
        return this.modify(i -> i.setAmount(i.getAmount() - amount));
    }

    public ItemBuilder setCustomModel(final int data) {
        if (this.itemStack.getType() == Material.PLAYER_HEAD) {
            return this.metaModify((SkullMeta meta) ->
                meta.setCustomModelData(data)
            );
        }
        return this.metaModify(meta ->
            meta.setCustomModelData(data)
        );
    }

    public ItemBuilder setEnchantments(@NotNull final Map<Enchantment, Integer> enchantments) {
        return this.modify(i -> {
            i.getEnchantments().forEach((k ,v) -> i.removeEnchantment(k));
            enchantments.forEach(i::addUnsafeEnchantment);
        });
    }

    public ItemBuilder addEnchant(@NotNull final Enchantment ench, final int level) {
        return this.modify(i -> i.addEnchantment(ench, level));
    }

    public ItemBuilder addEnchants(@NotNull final Map<Enchantment, Integer> enchantments) {
        return this.modify(i -> i.addEnchantments(enchantments));
    }

    public ItemBuilder unEnchant(@NotNull final Enchantment ench) {
        return modify(i -> i.removeEnchantment(ench));
    }

    public ItemBuilder unEnchants(@NotNull final List<Enchantment> enchantments) {
        enchantments.forEach(this::unEnchant);
        return this;
    }

    public ItemBuilder unEnchants(@NotNull final Enchantment ... enchs) {
        return modify(i -> Arrays.stream(enchs).forEach(i::removeEnchantment));
    }

    public ItemBuilder setLores(@NotNull final List<String> lores) {
        this.lore = lores;
        return this;
    }

    public ItemBuilder setLore(@NotNull final String lore, final int index) {
        if (index >= this.lore.size()) {
            this.lore.set(index, lore);
        }
        return this;
    }

    public ItemBuilder addLore(@NotNull final String lore) {
        this.lore.add(lore);
        return this;
    }

    public ItemBuilder addLores(@NotNull final List<String> lores) {
        lores.forEach(this::addLore);
        return this;
    }

    public ItemBuilder addLores(@NotNull final String ... lores) {
        Arrays.stream(lores).forEach(this::addLore);
        return this;
    }

    public ItemBuilder removeLore(final int index) {
        if (index <= lore.size()) {
            this.lore.remove(index);
        }
        return this;
    }

    public ItemBuilder removeLores(final int ... indexes) {
        Arrays.stream(indexes).forEach(this::removeLore);
        return this;
    }

    public ItemBuilder setFlags(@NotNull final List<ItemFlag> flags) {
        return this.modify(i -> {
            i.getItemFlags().forEach(i::removeItemFlags);
            i.addItemFlags(flags.toArray(new ItemFlag[flags.size()]));
        });
    }

    public ItemBuilder setFlags(@NotNull final ItemFlag... flags) {
        return this.modify(i -> {
            i.getItemFlags().forEach(i::removeItemFlags);
            i.addItemFlags(flags);
        });
    }

    public ItemBuilder addFlag(@NotNull final ItemFlag flag) {
        return this.modify(i -> i.addItemFlags(flag));
    }

    public ItemBuilder addFlags(@NotNull final List<ItemFlag> flags) {
        flags.forEach(this::addFlag);
        return this;
    }

    public ItemBuilder addFlags(@NotNull final ItemFlag ... flags) {
        Arrays.stream(flags).forEach(this::addFlag);
        return this;
    }

    public ItemBuilder unFlag(@NotNull final ItemFlag flag) {
        return this.modify(i -> i.removeItemFlags(flag));
    }

    public ItemBuilder unFlags(@NotNull final List<ItemFlag> flags) {
        return this.modify(i -> flags.forEach(i::removeItemFlags));
    }

    public ItemBuilder unFlags(@NotNull final ItemFlag ... flags) {
        return this.modify(i -> i.removeItemFlags(flags));
    }

    public ItemBuilder replace(@NotNull String key, @NotNull Object value) {
        name = name.replace("{" + key + "}", String.valueOf(value));
        lore = lore.stream().map(s -> s.replace("{" + key + "}", String.valueOf(value))).collect(Collectors.toList());
        return this;
    }

    public ItemBuilder modify(final Consumer<ItemStack> consumer) {
        ItemUtils.modify(this.itemStack, consumer);
        return this;
    }

    public ItemBuilder itemMetaModify(final Consumer<ItemMeta> consumer) {
        return this.modify(i -> {
            final ItemMeta meta = i.getItemMeta();
            consumer.accept(meta);
            i.setItemMeta(meta);
        });
    }

    @SuppressWarnings("unchecked")
    public <IM extends ItemMeta> ItemBuilder metaModify(final Consumer<IM> consumer) {
        return this.itemMetaModify(m -> consumer.accept((IM) m));
    }

    public ItemBuilder changeData(final Consumer<? super PersistentDataContainer> consumer) {
        return this.metaModify(meta -> consumer.accept(meta.getPersistentDataContainer()));
    }

    public <T, Z> ItemBuilder setData(@NotNull String key, final PersistentDataType<T, Z> type, final Z value) {
        return this.changeData(d -> d.set(key(key), type, value));
    }

    public ItemBuilder setData(@NotNull String key, final byte value) {
        return this.setData(key, PersistentDataType.BYTE, value);
    }

    public ItemBuilder setData(@NotNull String key, final byte[] values) {
        return this.setData(key, PersistentDataType.BYTE_ARRAY, values);
    }

    public ItemBuilder setData(@NotNull String key, final double value) {
        return this.setData(key, PersistentDataType.DOUBLE, value);
    }

    public ItemBuilder setData(@NotNull String key, final float value) {
        return this.setData(key, PersistentDataType.FLOAT, value);
    }

    public ItemBuilder setData(@NotNull String key, final int value) {
        return this.setData(key, PersistentDataType.INTEGER, value);
    }

    public ItemBuilder setData(@NotNull String key, final int[] values) {
        return this.setData(key, PersistentDataType.INTEGER_ARRAY, values);
    }

    public ItemBuilder setData(@NotNull String key, final long value) {
        return this.setData(key, PersistentDataType.LONG, value);
    }

    public ItemBuilder setData(@NotNull String key, final long[] values) {
        return this.setData(key, PersistentDataType.LONG_ARRAY, values);
    }

    public ItemBuilder setData(@NotNull String key, final short value) {
        return this.setData(key, PersistentDataType.SHORT, value);
    }

    public ItemBuilder setData(@NotNull String key, final String value) {
        return this.setData(key, PersistentDataType.STRING, value);
    }

    public ItemBuilder setData(@NotNull String key, final PersistentDataContainer value) {
        return this.setData(key, PersistentDataType.TAG_CONTAINER, value);
    }

    public ItemBuilder setData(@NotNull String key, final PersistentDataContainer[] values) {
        return this.setData(key, PersistentDataType.TAG_CONTAINER_ARRAY, values);
    }

    public Player getPlayer() {
        return this.player;
    }

    public @Nullable ItemGenerate getGenerate() {
        return this.generate;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public NamespacedKey key(@NotNull final String key) {
        return new NamespacedKey(HypePlugin.plugin, key);
    }

    public String parse(@NotNull final String value) {
        return PlaceholderAPI.setPlaceholders(this.player, value);
    }

    public ItemStack build() {
        if (this.generate != null) {
            this.generate.setMeta(this);
        } else {
            this.metaModify(meta -> {
                meta.displayName(AdventureUtils.toComponent(
                        this.parse(this.getName())
                ));
                if (!this.lore.isEmpty()) {
                    List<Component> lores = new ArrayList<>();
                    this.lore.forEach(l -> lores.add(
                            AdventureUtils.toComponent(
                                    this.parse(l)
                            ))
                    );
                    meta.lore(lores);
                }
            });
        }
        return this.itemStack.clone();
    }
}
