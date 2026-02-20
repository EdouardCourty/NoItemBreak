package com.ecourty.noitembreak.command;

import com.ecourty.noitembreak.config.ConfigManager;
import com.ecourty.noitembreak.config.NoItemBreakConfig;
import com.ecourty.noitembreak.util.ItemIdUtil;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;

public class NoItemBreakCommand {

    private static final SuggestionProvider<FabricClientCommandSource> ITEM_SUGGESTIONS =
        (ctx, builder) -> {
            BuiltInRegistries.ITEM.keySet().stream()
                .map(Object::toString)
                .filter(id -> id.startsWith(builder.getRemaining()))
                .forEach(builder::suggest);
            return builder.buildFuture();
        };

    private static final SuggestionProvider<FabricClientCommandSource> PROTECTED_ITEM_SUGGESTIONS =
        (ctx, builder) -> {
            ConfigManager.getConfig().protectedItems.stream()
                .filter(id -> id.startsWith(builder.getRemaining()))
                .forEach(builder::suggest);
            return builder.buildFuture();
        };

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(
                ClientCommandManager.literal("noitembreak")
                    .then(ClientCommandManager.literal("add")
                        .then(ClientCommandManager.argument("item", StringArgumentType.greedyString())
                            .suggests(ITEM_SUGGESTIONS)
                            .executes(NoItemBreakCommand::add)))
                    .then(ClientCommandManager.literal("remove")
                        .then(ClientCommandManager.argument("item", StringArgumentType.greedyString())
                            .suggests(PROTECTED_ITEM_SUGGESTIONS)
                            .executes(NoItemBreakCommand::remove)))
                    .then(ClientCommandManager.literal("list")
                        .executes(NoItemBreakCommand::list))
            )
        );
    }

    private static int add(CommandContext<FabricClientCommandSource> ctx) {
        String item = ItemIdUtil.normalize(StringArgumentType.getString(ctx, "item"));
        if (!ItemIdUtil.exists(item)) {
            ctx.getSource().sendFeedback(Component.literal("Unknown item: " + item).withStyle(ChatFormatting.RED));
            return 0;
        }
        NoItemBreakConfig config = ConfigManager.getConfig();
        if (config.protectedItems.contains(item)) {
            ctx.getSource().sendFeedback(Component.literal(item + " is already protected.").withStyle(ChatFormatting.YELLOW));
            return 0;
        }
        config.protectedItems.add(item);
        ConfigManager.save();
        ctx.getSource().sendFeedback(Component.literal("+ Added " + item + " to protected items.").withStyle(ChatFormatting.GREEN));
        return 1;
    }

    private static int remove(CommandContext<FabricClientCommandSource> ctx) {
        String item = ItemIdUtil.normalize(StringArgumentType.getString(ctx, "item"));
        NoItemBreakConfig config = ConfigManager.getConfig();
        if (!config.protectedItems.remove(item)) {
            ctx.getSource().sendFeedback(Component.literal(item + " is not in the protected list.").withStyle(ChatFormatting.YELLOW));
            return 0;
        }
        ConfigManager.save();
        ctx.getSource().sendFeedback(Component.literal("- Removed " + item + " from protected items.").withStyle(ChatFormatting.RED));
        return 1;
    }

    private static int list(CommandContext<FabricClientCommandSource> ctx) {
        NoItemBreakConfig config = ConfigManager.getConfig();
        if (config.protectedItems.isEmpty()) {
            ctx.getSource().sendFeedback(Component.literal("No protected items.").withStyle(ChatFormatting.GRAY));
            return 0;
        }
        ctx.getSource().sendFeedback(Component.literal("Protected items:").withStyle(ChatFormatting.GOLD));
        config.protectedItems.forEach(item ->
            ctx.getSource().sendFeedback(Component.literal(" - " + item).withStyle(ChatFormatting.GRAY))
        );
        return config.protectedItems.size();
    }
}
