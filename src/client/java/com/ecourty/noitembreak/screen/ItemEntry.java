package com.ecourty.noitembreak.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemEntry extends ObjectSelectionList.Entry<ItemEntry> {

    private static final int ICON_SIZE = 16;
    private static final int ICON_TEXT_GAP = 4;
    private static final int TEXT_Y_TOP = 2;
    private static final int TEXT_Y_BOTTOM = 12;
    private static final int COLOR_INVALID = 0xFFFF5555;

    final String itemId;
    private final ItemList owner;
    private final ItemStack stack;
    private final Component displayName;
    private final boolean valid;

    ItemEntry(ItemList owner, String itemId) {
        this.owner = owner;
        this.itemId = itemId;
        ItemStack resolved;
        boolean isValid;
        try {
            var item = BuiltInRegistries.ITEM.getValue(Identifier.parse(itemId));
            resolved = new ItemStack(item);
            isValid = true;
        } catch (Exception e) {
            resolved = new ItemStack(Items.BARRIER);
            isValid = false;
        }
        this.stack = resolved;
        this.valid = isValid;
        this.displayName = stack.getHoverName();
    }

    @Override
    public Component getNarration() {
        return displayName;
    }

    @Override
    public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY,
                              boolean hovered, float partialTick) {
        var font = Minecraft.getInstance().font;
        int iconX = getContentX();
        int iconY = getContentY() + (getContentHeight() - ICON_SIZE) / 2;
        int textX = iconX + ICON_SIZE + ICON_TEXT_GAP;
        guiGraphics.renderItem(stack, iconX, iconY);
        guiGraphics.drawString(font, displayName, textX, getContentY() + TEXT_Y_TOP, NoItemBreakConfigScreen.COLOR_WHITE, true);
        guiGraphics.drawString(font, itemId, textX, getContentY() + TEXT_Y_BOTTOM, valid ? NoItemBreakConfigScreen.COLOR_GRAY : COLOR_INVALID, false);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean bl) {
        owner.setSelected(this);
        return true;
    }
}
