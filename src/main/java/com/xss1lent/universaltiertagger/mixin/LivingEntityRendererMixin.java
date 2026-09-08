package com.xss1lent.universaltiertagger.mixin;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.display.TierComponentFormatter;
import com.xss1lent.universaltiertagger.display.TierDisplayManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Inject(
            method = "getDisplayName",
            at = @At("RETURN"),
            cancellable = true
    )
    private void universalTierTagger$modifyDisplayName(
            Entity entity,
            CallbackInfoReturnable<Component> cir
    ) {

        if (UniversalTierTaggerClient.CONFIG == null) {
            return;
        }

        if (!UniversalTierTaggerClient.CONFIG.showNametags) {
            return;
        }

        if (!(entity instanceof AbstractClientPlayer player)) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (UniversalTierTaggerClient.CONFIG.hideOwnTag
                && minecraft.player == player) {
            return;
        }

        String username = player.getGameProfile().name();

        if (username == null || username.isBlank()) {
            return;
        }

        Component originalName = cir.getReturnValue();

        if (originalName == null) {
            return;
        }

        Component result = Component.empty();

        // Secondary tier
        if (UniversalTierTaggerClient.CONFIG.showSecondaryTierlist) {

            TierDisplayManager.DisplayTier secondaryTier =
                    TierDisplayManager.getSecondaryTier(username);

            if (secondaryTier != null) {

                Component secondary =
                        TierComponentFormatter.formatSecondary(
                                secondaryTier
                        );

                if (!secondary.getString().isBlank()) {

                    result = result.copy()
                            .append(secondary)
                            .append(Component.literal(" "));
                }
            }
        }

        // Primary tier
        TierDisplayManager.DisplayTier primaryTier =
                TierDisplayManager.getPrimaryTier(username);

        if (primaryTier != null) {

            Component primary =
                    TierComponentFormatter.formatPrimary(
                            primaryTier
                    );

            if (!primary.getString().isBlank()) {

                result = result.copy()
                        .append(primary)
                        .append(Component.literal(" "));
            }
        }

        if (!result.getString().isBlank()) {

            cir.setReturnValue(
                    result.copy()
                            .append(originalName)
            );
        }
    }
}
