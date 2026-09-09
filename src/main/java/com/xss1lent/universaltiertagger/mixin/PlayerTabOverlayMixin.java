package com.xss1lent.universaltiertagger.mixin;

import com.mojang.authlib.GameProfile;
import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.display.TierComponentFormatter;
import com.xss1lent.universaltiertagger.display.TierDisplayManager;
import net.minecraft.client.Minecraft;
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

        if (playerInfo == null) {
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

        // Hide own tier if enabled
        if (UniversalTierTaggerClient.CONFIG.hideOwnTag) {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.player != null
                    && minecraft.player.getGameProfile().name() != null
                    && username.equalsIgnoreCase(
                    minecraft.player.getGameProfile().name()
            )) {
                return;
            }
        }

        // TAB ONLY SHOWS THE PRIMARY TIERLIST
        TierDisplayManager.DisplayTier primaryTier =
                TierDisplayManager.getPrimaryTier(username);

        if (primaryTier == null) {
            return;
        }

        Component primaryComponent =
                TierComponentFormatter.formatPrimary(primaryTier);

        if (primaryComponent == null
                || primaryComponent.getString().isBlank()) {
            return;
        }

        Component originalName = cir.getReturnValue();

        if (originalName == null) {
            return;
        }

        cir.setReturnValue(
                primaryComponent
                        .copy()
                        .append(Component.literal(" "))
                        .append(originalName)
        );
    }
}
