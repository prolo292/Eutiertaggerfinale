package com.xss1lent.universaltiertagger.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerInfo.class)
public abstract class PlayerListEntryMixin
        implements PlayerInfoAccessor {

    @Shadow
    public abstract GameProfile getProfile();

    @Override
    public String universalTierTagger$getUsername() {

        GameProfile profile = getProfile();

        if (profile == null) {
            return "";
        }

        String name = profile.name();

        return name != null ? name : "";
    }
}
