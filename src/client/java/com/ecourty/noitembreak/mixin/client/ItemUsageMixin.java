package com.ecourty.noitembreak.mixin.client;

import com.ecourty.noitembreak.config.ConfigManager;
import com.ecourty.noitembreak.config.NoItemBreakConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class ItemUsageMixin {

    private static final int DURABILITY_WARNING_THRESHOLD = 1;

    @Inject(at = @At("HEAD"), method = "startAttack", cancellable = true)
    private void onStartAttack(CallbackInfoReturnable<Boolean> cir) {
        if (shouldCancel()) cir.setReturnValue(false);
    }

    @Inject(at = @At("HEAD"), method = "continueAttack", cancellable = true)
    private void onContinueAttack(boolean attacking, CallbackInfo ci) {
        if (shouldCancel()) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "startUseItem", cancellable = true)
    private void onStartUseItem(CallbackInfo ci) {
        if (shouldCancel()) ci.cancel();
    }

    private boolean shouldCancel() {
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.player == null) return false;

        NoItemBreakConfig config = ConfigManager.getConfig();
        if (!config.enabled) return false;

        ItemStack mainHandItem = mc.player.getMainHandItem();
        if (mainHandItem.isEmpty() || !mainHandItem.isDamageableItem()) return false;

        if (mainHandItem.getDamageValue() >= mainHandItem.getMaxDamage() - DURABILITY_WARNING_THRESHOLD) {
            String itemId = BuiltInRegistries.ITEM.getKey(mainHandItem.getItem()).toString();
            if (config.protectedItems.contains(itemId)) {
                mc.player.displayClientMessage(
                    Component.translatable("message.noitembreak.durability_warning"),
                    true
                );
                return true;
            }
        }
        return false;
    }
}
