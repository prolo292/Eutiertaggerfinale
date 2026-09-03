package com.xss1lent.universaltiertagger.render;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;

public class SecondaryNametagRenderer {

    /**
     * Returns the secondary tier text that should appear
     * above the normal player nametag.
     */
    public static Component getSecondaryNametag(
            AbstractClientPlayer player
    ) {

        if (player == null) {
            return Component.empty();
        }

        if (UniversalTierTaggerClient.CONFIG == null) {
            return Component.empty();
        }

        if (!UniversalTierTaggerClient.CONFIG.showNametags) {
            return Component.empty();
        }

        if (!UniversalTierTaggerClient.CONFIG.showSecondaryTierlist) {
            return Component.empty();
        }

        Minecraft client = Minecraft.getInstance();

        if (client.player == null) {
            return Component.empty();
        }

        if (UniversalTierTaggerClient.CONFIG.hideOwnTag
                && player == client.player) {
            return Component.empty();
        }

        String username = player.getGameProfile().name();

        if (username == null || username.isBlank()) {
            return Component.empty();
        }

        String text =
                TierNametagRenderer.getSecondaryText(username);

        if (text == null || text.isBlank()) {
            return Component.empty();
        }

        return Component.literal(text);
    }
}
