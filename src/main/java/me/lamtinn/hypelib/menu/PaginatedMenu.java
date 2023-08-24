package me.lamtinn.hypelib.menu;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class PaginatedMenu extends Menu {

    private int page = 1;
    private int itemsPerPage;
    private int maxPage;
    private List<?> data;

    public PaginatedMenu(@NotNull final String title, final int rows) {
        super(title, rows);
        this.data = List.of();
    }

    public PaginatedMenu(final int rows) {
        super(rows);
        this.data = List.of();
    }

    public abstract List<String> contentSlots();

    public abstract void loop(@NotNull Object object, final int index);

    public abstract void borders();

    @Override
    public void setButtons() {
        List<Integer> slots = Arrays.stream(this.getSlots(contentSlots()))
                .boxed()
                .toList();

        borders();
        setItemsPerPage(slots.size());
        setMaxPage((int) Math.ceil((double) getDataSize() / getItemsPerPage()));

        List<Object> data = (List<Object>) getData();
        int items = getItemsPerPage();

        int startIndex = (getPage() - 1) * items;
        int endIndex = Math.min(startIndex + items, getDataSize());

        for (int i = startIndex; i < endIndex; i++) {
            loop(data.get(i), slots.get(i - startIndex));
        }
    }

    public List<?> getData() {
        return data;
    }

    public void setData(final List<?> data) {
        this.data = data;
    }

    public boolean next() {
        if (getPage() < getMaxPage()) {
            setPage(getPage() + 1);
            reloadButtons();
            return true;
        } else {
            return false;
        }
    }

    public boolean previous() {
        if (getPage() == 1) {
            return false;
        } else {
            setPage(getPage() - 1);
            reloadButtons();
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
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getDataSize() {
        return data.size();
    }
}
