package com.ecourty.noitembreak.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;

import java.util.List;

public class ItemList extends ObjectSelectionList<ItemEntry> {

    private final int rowWidth;

    public ItemList(Minecraft mc, int width, int height, int y, int itemHeight, int rowWidth) {
        super(mc, width, height, y, itemHeight);
        this.rowWidth = rowWidth;
    }

    public void addItem(String itemId) {
        ItemEntry entry = new ItemEntry(this, itemId);
        addEntry(entry);
        scrollToEntry(entry);
    }

    public void removeItem(ItemEntry entry) {
        List<ItemEntry> items = children();
        int index = items.indexOf(entry);
        boolean wasLast = index == items.size() - 1;
        removeEntry(entry);
        if (children().isEmpty()) return;
        ItemEntry next = children().get(wasLast ? index - 1 : index);
        setSelected(next);
        if (wasLast) setScrollAmount(Double.MAX_VALUE);
    }

    public void reset(List<String> items) {
        clearEntries();
        items.forEach(id -> addEntry(new ItemEntry(this, id)));
        if (!children().isEmpty()) setScrollAmount(Double.MAX_VALUE);
    }

    @Override
    public int getRowWidth() {
        return rowWidth;
    }
}
