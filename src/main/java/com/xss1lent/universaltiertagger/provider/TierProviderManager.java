package com.xss1lent.universaltiertagger.provider;

import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TierProviderManager {

    private final Map<TierlistType, TierProvider> providers =
            new EnumMap<>(TierlistType.class);

    public TierProviderManager() {

        // European Tierlist
        registerProvider(new EuropeanTierProvider());

        // MCTiers
        registerProvider(new MCTiersProvider());

        // MCPVP current/BETA tiers only
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
     * Fetches player tiers from the selected tierlist.
     */
    public CompletableFuture<PlayerTierData> fetchPlayerTiers(
            TierlistType type,
            String username
    ) {

        TierProvider provider = getProvider(type);

        if (provider == null) {
            return CompletableFuture.completedFuture(
                    new PlayerTierData(username)
            );
        }

        return provider.fetchPlayerTiers(username);
    }

    /**
     * Checks if a provider is registered.
     */
    public boolean hasProvider(TierlistType type) {
        return providers.containsKey(type);
    }
}
