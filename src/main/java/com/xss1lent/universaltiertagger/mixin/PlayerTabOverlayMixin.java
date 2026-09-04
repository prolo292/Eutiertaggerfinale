package com.xss1lent.universaltiertagger.mixin;

import com.mojang.authlib.GameProfile;
import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.display.TierComponentFormatter;
import com.xss1lent.universaltiertagger.display.TierDisplayManager;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

    @Inject(
            method = "getNameForDisplay",
            at = @At("RETURN"),
            cancellable = true
    )
    private void universalTierTagger$modifyTabName(
            PlayerInfo playerInfo,
            CallbackInfoReturnable<Component> cir
    ) {

        if (UniversalTierTaggerClient.CONFIG == null
                || !UniversalTierTaggerClient.CONFIG.showInTab) {
            return;
        }

        GameProfile profile = playerInfo.getProfile();

        if (profile == null) {
            return;
        }

        String username = profile.name();

        if (username == null || username.isBlank()) {
            return;
        }

        // Optional: hide our own tier tag
        if (UniversalTierTaggerClient.CONFIG.hideOwnTag
                && UniversalTierTaggerClient.CONFIG != null) {

            if (net.minecraft.client.Minecraft.getInstance().player != null
                    && username.equalsIgnoreCase(
                    net.minecraft.client.Minecraft.getInstance()
                            .player
                            .getGameProfile()
                            .name()
            )) {
                return;
            }
        }

        Component result = Component.empty();

        // Secondary tier first
        if (UniversalTierTaggerClient.CONFIG.showSecondaryTierlist) {

            TierDisplayManager.DisplayTier secondaryTier =
                    TierDisplayManager.getSecondaryTier(username);

            if (secondaryTier != null) {

                Component secondaryComponent =
                        TierComponentFormatter.formatSecondary(
                                secondaryTier
                        );

                if (!secondaryComponent.getString().isBlank()) {

                    result = result.copy()
                            .append(secondaryComponent)
                            .append(Component.literal(" "));
                }
            }
        }

        // Primary tier
        TierDisplayManager.DisplayTier primaryTier =
                TierDisplayManager.getPrimaryTier(username);

        if (primaryTier != null) {

            Component primaryComponent =
                    TierComponentFormatter.formatPrimary(
                            primaryTier
                    );

            if (!primaryComponent.getString().isBlank()) {

                result = result.copy()
                        .append(primaryComponent)
                        .append(Component.literal(" "));
            }
        }

        // No tier information available
        if (result.getString().isBlank()) {
            return;
        }

        Component originalName = cir.getReturnValue();

        if (originalName == null) {
            return;
        }

        cir.setReturnValue(
                result.copy()
                        .append(originalName)
        );
    }
}
