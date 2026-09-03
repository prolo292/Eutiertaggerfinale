package com.xss1lent.universaltiertagger.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerInfo.class)
public abstract class PlayerListEntryMixin {

    @Shadow
    public abstract GameProfile getProfile();
}
