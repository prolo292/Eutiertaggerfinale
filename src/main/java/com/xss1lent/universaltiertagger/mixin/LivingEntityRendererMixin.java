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
            method = "getNameTag",
            at = @At("RETURN"),
            cancellable = true
    )
    private void universalTierTagger$modifyNameTag(
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

        TierDisplayManager.DisplayTier primary =
                TierDisplayManager.getPrimaryTier(username);

        if (primary == null) {
            return;
        }

        Component tier =
                TierComponentFormatter.formatPrimary(primary);

        if (tier == null || tier.getString().isBlank()) {
            return;
        }

        cir.setReturnValue(
                tier.copy()
                        .append(Component.literal(" "))
                        .append(originalName)
        );
    }
}
