package com.xss1lent.universaltiertagger.mixin;

import com.xss1lent.universaltiertagger.render.TierNametagRenderer;
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

        String primary =
                TierNametagRenderer.getPrimaryText(username);

        String secondary =
                TierNametagRenderer.getSecondaryText(username);

        boolean hasPrimary =
                primary != null && !primary.isBlank();

        boolean hasSecondary =
                secondary != null && !secondary.isBlank();

        if (!hasPrimary && !hasSecondary) {
            return;
        }

        Component result = Component.empty();

        /*
         * Secondary tier goes above everything.
         */
        if (hasSecondary) {
            result = Component.literal(secondary + "\n");
        }

        /*
         * Primary tier goes next to the player's name.
         */
        if (hasPrimary) {
            result = result.copy()
                    .append(Component.literal(primary + " "));
        }

        result = result.copy()
                .append(originalName);

        cir.setReturnValue(result);
    }
}
