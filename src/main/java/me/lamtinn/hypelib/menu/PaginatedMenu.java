package me.lamtinn.hypelib.menu;

import me.lamtinn.hypelib.utils.PluginUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class PaginatedMenu extends Menu {

    private int page = 1;
    private int itemsPerPage;
    private final int maxPage = (int) Math.ceil((double) getDataSize() / itemsPerPage);

    private List<?> data;

    public PaginatedMenu(String title, int rows) {
        super(title, rows);
        this.data = Collections.emptyList();
    }

    public abstract List<String> contentSlots();

    public abstract void loop(@NotNull Object object, final int index);

    public abstract void borders();

    @Override
    public void setButtons() {
        int[] arr = this.getSlots(this.contentSlots());
        final List<Integer> slots = Arrays.stream(arr).boxed().sorted().toList();

        this.borders();
        this.setItemsPerPage(slots.size());

        List<Object> data = (List<Object>) getData();
        int items = this.getItemsPerPage();

        int startIndex = (this.page - 1) * items;
        int endIndex = Math.min(startIndex + items, getDataSize());

        for (int i = startIndex; i < endIndex; i++) {
            this.loop(data.get(i), slots.get(i - startIndex));
        }
    }

    public List<?> getData() {
        return this.data;
    }

    public void setData(final List<?> data) {
        this.data = data;
    }

    public boolean next() {
        if (this.page < this.getMaxPage()) {
            page = page + 1;
            this.reloadButtons();
            return true;
        } else {
            return false;
        }
    }

    public boolean previous() {
        if (this.page == 1) {
            return false;
        } else {
            page = page - 1;
            this.reloadButtons();
            return true;
        }
    }

    public void setItemsPerPage(final int amount) {
        this.itemsPerPage = amount;
    }

    public int getPage() {
        return this.page;
    }

    public int getItemsPerPage() {
        return this.itemsPerPage;
    }

    public int getMaxPage() {
        return this.maxPage;
    }

    public int getDataSize() {
        return this.data == null ? 0 : this.data.size();
    }

    public int[] getSlots(final List<String> slots) {
        List<Integer> arr = new ArrayList<>();
        for (String input : slots) {
            if (input.contains("-") && input.split("-", 2).length == 2) {
                String[] parts = input.split("-", 2);
                for (int j = Integer.parseInt(parts[0]); j <= Integer.parseInt(parts[1]); j++) {
                    if (j == -1 || j > this.slots()) continue;
                    arr.add(j);
                }
            } else {
                int i = PluginUtils.isInteger(input);
                if (i == -1 || i > slots()) continue;
                arr.add(i);
            }
        }
        return arr.stream().mapToInt(i -> i).toArray();
    }
}
