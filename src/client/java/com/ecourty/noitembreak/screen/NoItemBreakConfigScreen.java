package com.ecourty.noitembreak.screen;

import com.ecourty.noitembreak.config.ConfigManager;
import com.ecourty.noitembreak.config.NoItemBreakConfig;
import com.ecourty.noitembreak.util.ItemIdUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;

public class NoItemBreakConfigScreen extends Screen {

    private static final int LIST_TOP = 48;
    private static final int LIST_BOTTOM_MARGIN = 80;
    private static final int DONE_CANCEL_OFFSET = 26;
    private static final int CONTENT_WIDTH = 320;
    private static final int ITEM_HEIGHT = 24;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_WIDTH_HALF = (CONTENT_WIDTH - 5) / 2;
    private static final int BUTTON_WIDTH_SMALL = 45;
    private static final int INPUT_WIDTH = CONTENT_WIDTH - BUTTON_WIDTH_SMALL - 5;
    static final int COLOR_WHITE = 0xFFFFFFFF;
    static final int COLOR_GRAY = 0xFFAAAAAA;

    private final Screen parentScreen;
    private final NoItemBreakConfig config;

    private ItemList itemList;
    private EditBox newItemInput;

    public NoItemBreakConfigScreen(Screen parentScreen) {
        super(Component.translatable("screen.noitembreak.title"));
        this.parentScreen = parentScreen;
        this.config = ConfigManager.getConfig().copy();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int left = centerX - CONTENT_WIDTH / 2;
        int right = centerX + CONTENT_WIDTH / 2;
        int listBottom = this.height - LIST_BOTTOM_MARGIN;

        // Enable toggle
        addRenderableWidget(
            CycleButton.onOffBuilder(config.enabled)
            .create(
                left, 22, CONTENT_WIDTH, BUTTON_HEIGHT,
                Component.translatable("options.noitembreak.enabled"),
                (btn, value) -> config.enabled = value
            )
        );

        // Item list
        itemList = new ItemList(minecraft, this.width, listBottom - LIST_TOP, LIST_TOP, ITEM_HEIGHT, CONTENT_WIDTH);
        config.protectedItems.forEach(itemList::addItem);
        addRenderableWidget(itemList);

        // New item input + Add button
        newItemInput = new EditBox(font, left, listBottom + 5, INPUT_WIDTH, BUTTON_HEIGHT, Component.empty());
        newItemInput.setHint(Component.translatable("options.noitembreak.item_hint"));
        newItemInput.setMaxLength(256);
        addRenderableWidget(newItemInput);

        addRenderableWidget(Button.builder(
            Component.translatable("options.noitembreak.add"),
            btn -> {
                String id = ItemIdUtil.normalize(newItemInput.getValue().trim());
                if (!id.isEmpty() && ItemIdUtil.exists(id) && !config.protectedItems.contains(id)) {
                    config.protectedItems.add(id);
                    itemList.addItem(id);
                    newItemInput.setValue("");
                }
            }
        ).bounds(right - BUTTON_WIDTH_SMALL, listBottom + 5, BUTTON_WIDTH_SMALL, BUTTON_HEIGHT).build());

        // Remove selected + Reset defaults
        addRenderableWidget(Button.builder(
            Component.translatable("options.noitembreak.remove"),
            btn -> {
                ItemEntry selected = itemList.getSelected();
                if (selected != null) {
                    config.protectedItems.remove(selected.itemId);
                    itemList.removeItem(selected);
                }
            }
        ).bounds(left, listBottom + 30, BUTTON_WIDTH_HALF, BUTTON_HEIGHT).build());

        addRenderableWidget(Button.builder(
            Component.translatable("options.noitembreak.reset"),
            btn -> {
                config.protectedItems = new ArrayList<>(NoItemBreakConfig.DEFAULT_PROTECTED_ITEMS);
                itemList.reset(config.protectedItems);
            }
        ).bounds(right - BUTTON_WIDTH_HALF, listBottom + 30, BUTTON_WIDTH_HALF, BUTTON_HEIGHT).build());

        // Cancel / Done
        addRenderableWidget(Button.builder(
            Component.translatable("gui.cancel"),
            btn -> minecraft.setScreen(parentScreen)
        ).bounds(left, this.height - DONE_CANCEL_OFFSET, BUTTON_WIDTH_HALF, BUTTON_HEIGHT).build());

        addRenderableWidget(Button.builder(
            Component.translatable("gui.done"),
            btn -> {
                NoItemBreakConfig current = ConfigManager.getConfig();
                current.enabled = config.enabled;
                current.protectedItems = config.protectedItems;
                ConfigManager.save();
                minecraft.setScreen(parentScreen);
            }
        ).bounds(right - BUTTON_WIDTH_HALF, this.height - DONE_CANCEL_OFFSET, BUTTON_WIDTH_HALF, BUTTON_HEIGHT).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(font, this.title, this.width / 2, 8, COLOR_WHITE);
    }
}
