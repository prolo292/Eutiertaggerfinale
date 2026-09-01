package com.xss1lent.universaltiertagger.provider;

import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;

import java.util.concurrent.CompletableFuture;

public interface TierProvider {

    /**
     * Returns the tierlist handled by this provider.
     */
    TierlistType getTierlistType();

    /**
     * Fetches all known tiers for a player.
     *
     * @param username Minecraft username
     * @return asynchronous player tier data
     */
    CompletableFuture<PlayerTierData> fetchPlayerTiers(String username);

    /**
     * Returns true if this provider is currently available.
     */
    default boolean isAvailable() {
        return true;
    }
}
