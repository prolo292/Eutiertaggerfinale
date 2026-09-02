package com.xss1lent.universaltiertagger.data;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;

import java.util.concurrent.CompletableFuture;

public class TierDataManager {

    public static CompletableFuture<PlayerTierData> getPlayerTiers(
            String username,
            TierlistType tierlist
    ) {
        PlayerTierData cached = UniversalTierTaggerClient.CACHE.get(
                tierlist,
                username,
                UniversalTierTaggerClient.CONFIG.refreshIntervalSeconds
        );

        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }

        return UniversalTierTaggerClient.PROVIDERS
                .fetchPlayerTiers(tierlist, username)
                .thenApply(data -> {
                    UniversalTierTaggerClient.CACHE.put(
                            tierlist,
                            username,
                            data
                    );

                    return data;
                });
    }

    public static void clearCache() {
        UniversalTierTaggerClient.CACHE.clear();
    }
}
