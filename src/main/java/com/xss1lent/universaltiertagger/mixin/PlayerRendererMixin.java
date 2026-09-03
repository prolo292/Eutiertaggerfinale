package com.xss1lent.universaltiertagger.mixin;

import com.xss1lent.universaltiertagger.display.TierComponentFormatter;
import com.xss1lent.universaltiertagger.display.TierDisplayManager;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Inject(
            method = "getNameTag",
            at = @At("RETURN"),
            cancellable = true
    )
    private void universalTierTagger$modifyNameTag(
            AbstractClientPlayer player,
            CallbackInfoReturnable<Component> cir
    ) {

        if (player == null) {
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

        TierDisplayManager.DisplayTier primaryTier =
                TierDisplayManager.getPrimaryTier(username);

        TierDisplayManager.DisplayTier secondaryTier =
                TierDisplayManager.getSecondaryTier(username);

        Component result = Component.empty();

        // Secondary tier above the player's name
        if (secondaryTier != null) {

            result = result.copy()
                    .append(
                            TierComponentFormatter.formatSecondary(
                                    secondaryTier
                            )
                    )
                    .append(Component.literal("\n"));
        }

        // Primary tier next to player's name
        if (primaryTier != null) {

            result = result.copy()
                    .append(
                            TierComponentFormatter.formatPrimary(
                                    primaryTier
                            )
                    )
                    .append(Component.literal(" "));
        }

        // Don't modify players without tier data
        if (primaryTier == null && secondaryTier == null) {
            return;
        }

        result = result.copy().append(originalName);

        cir.setReturnValue(result);
    }
}
