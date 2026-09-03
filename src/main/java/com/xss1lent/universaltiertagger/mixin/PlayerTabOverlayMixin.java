package com.xss1lent.universaltiertagger.mixin;

import com.mojang.authlib.GameProfile;
import com.xss1lent.universaltiertagger.render.TierTabRenderer;
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

        String tierText =
                TierTabRenderer.getTabText(username);

        if (tierText == null || tierText.isBlank()) {
            return;
        }

        Component originalName = cir.getReturnValue();

        cir.setReturnValue(
                Component.literal(tierText + " ")
                        .append(originalName)
        );
    }
}
