package com.xss1lent.universaltiertagger.mixin;

import com.mojang.authlib.GameProfile;
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

        GameProfile profile = playerInfo.getProfile();

        if (profile == null) {
            return;
        }

        String username = profile.name();

        if (username == null || username.isBlank()) {
            return;
        }

        TierDisplayManager.DisplayTier tier =
                TierDisplayManager.getPrimaryTier(username);

        if (tier == null) {
            return;
        }

        Component tierComponent =
                TierComponentFormatter.formatPrimary(tier);

        Component originalName = cir.getReturnValue();

        if (originalName == null) {
            return;
        }

        cir.setReturnValue(
                tierComponent.copy()
                        .append(Component.literal(" "))
                        .append(originalName)
        );
    }
}
