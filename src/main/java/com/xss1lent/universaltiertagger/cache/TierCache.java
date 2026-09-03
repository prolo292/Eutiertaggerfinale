package com.xss1lent.universaltiertagger.provider;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TierProviderManager {

    private static final int CACHE_DURATION_SECONDS = 300;

    private final Map<TierlistType, TierProvider> providers =
            new EnumMap<>(TierlistType.class);

    public TierProviderManager() {

        registerProvider(new EuropeanTierProvider());
        registerProvider(new MCTiersProvider());
        registerProvider(new MCPVPProvider());
    }

    /**
     * Registers a tier provider.
     */
    public void registerProvider(TierProvider provider) {

        if (provider == null) {
            return;
        }

        providers.put(
                provider.getTierlistType(),
                provider
        );
    }

    /**
     * Gets a provider by tierlist type.
     */
    public TierProvider getProvider(TierlistType type) {
        return providers.get(type);
    }

    /**
     * Fetches player tiers.
     *
     * First checks cache.
     * If no valid cache exists, fetches from the provider.
     */
    public CompletableFuture<PlayerTierData> fetchPlayerTiers(
            TierlistType type,
            String username
    ) {

        if (type == null || username == null || username.isBlank()) {
            return CompletableFuture.completedFuture(
                    new PlayerTierData(username)
            );
        }

        // Check cache first
        if (UniversalTierTaggerClient.CACHE != null) {

            PlayerTierData cachedData =
                    UniversalTierTaggerClient.CACHE.get(
                            type,
                            username,
                            CACHE_DURATION_SECONDS
                    );

            if (cachedData != null) {
                return CompletableFuture.completedFuture(cachedData);
            }
        }

        TierProvider provider = getProvider(type);

        if (provider == null) {
            return CompletableFuture.completedFuture(
                    new PlayerTierData(username)
            );
        }

        // Fetch from API and cache result
        return provider.fetchPlayerTiers(username)
                .thenApply(data -> {

                    if (data == null) {
                        data = new PlayerTierData(username);
                    }

                    if (UniversalTierTaggerClient.CACHE != null) {
                        UniversalTierTaggerClient.CACHE.put(
                                type,
                                username,
                                data
                        );
                    }

                    return data;
                })
                .exceptionally(exception -> {

                    UniversalTierTaggerClient.LOGGER.warn(
                            "Failed to fetch {} tiers for {}",
                            type,
                            username,
                            exception
                    );

                    return new PlayerTierData(username);
                });
    }

    /**
     * Checks if a provider is registered.
     */
    public boolean hasProvider(TierlistType type) {
        return providers.containsKey(type);
    }

    /**
     * Clears all cached tier data.
     */
    public void clearCache() {

        if (UniversalTierTaggerClient.CACHE != null) {
            UniversalTierTaggerClient.CACHE.clear();
        }
    }
}
