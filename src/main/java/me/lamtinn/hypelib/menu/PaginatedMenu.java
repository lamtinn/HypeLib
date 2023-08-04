package me.lamtinn.hypelib.menu;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class PaginatedMenu extends Menu {

    private int page = 1;
    private int itemsPerPage;
    private int maxPage;
    private List<?> data;

    public PaginatedMenu(@NotNull final String title, final int rows) {
        super(title, rows);
        this.data = Collections.emptyList();
    }

    public PaginatedMenu(final int rows) {
        super(rows);
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
        this.setMaxPage((int) Math.ceil((double) getDataSize() / getItemsPerPage()));

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

    public void setMaxPage(final int maxPage) {
        this.maxPage = maxPage;
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
}
