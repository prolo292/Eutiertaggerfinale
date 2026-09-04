package com.xss1lent.universaltiertagger.mixin;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.display.TierComponentFormatter;
import com.xss1lent.universaltiertagger.display.TierDisplayManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class PlayerEntityRendererMixin {

    @Inject(
            method = "getDisplayName",
            at = @At("RETURN"),
            cancellable = true
    )
    private void universalTierTagger$modifyDisplayName(
            CallbackInfoReturnable<Component> cir
    ) {

        if (UniversalTierTaggerClient.CONFIG == null
                || !UniversalTierTaggerClient.CONFIG.showNametags) {
            return;
        }

        AbstractClientPlayer player =
                (AbstractClientPlayer) (Object) this;

        String username = player.getGameProfile().name();

        if (username == null || username.isBlank()) {
            return;
        }

        if (UniversalTierTaggerClient.CONFIG.hideOwnTag
                && Minecraft.getInstance().player != null
                && player == Minecraft.getInstance().player) {
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

                result = result.copy()
                        .append(secondary)
                        .append(Component.literal(" "));
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

            result = result.copy()
                    .append(primary)
                    .append(Component.literal(" "));
        }

        if (!result.getString().isBlank()) {

            cir.setReturnValue(
                    result.copy()
                            .append(originalName)
            );
        }
    }
}
