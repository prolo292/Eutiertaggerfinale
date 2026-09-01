package com.xss1lent.universaltiertagger.cache;

import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TierCache {

    private final Map<String, CachedPlayerData> cache =
            new ConcurrentHashMap<>();

    /**
     * Stores player tier data in the cache.
     */
    public void put(
            TierlistType tierlist,
            String username,
            PlayerTierData data
    ) {
        if (tierlist == null || username == null || data == null) {
            return;
        }

        String key = createKey(tierlist, username);

        cache.put(
                key,
                new CachedPlayerData(
                        data,
                        System.currentTimeMillis()
                )
        );
    }

    /**
     * Gets cached player data.
     */
    public PlayerTierData get(
            TierlistType tierlist,
            String username,
            int maxAgeSeconds
    ) {
        if (tierlist == null || username == null) {
            return null;
        }

        String key = createKey(tierlist, username);

        CachedPlayerData cachedData = cache.get(key);

        if (cachedData == null) {
            return null;
        }

        long ageMilliseconds =
                System.currentTimeMillis()
                        - cachedData.timestamp;

        long maxAgeMilliseconds =
                maxAgeSeconds * 1000L;

        if (ageMilliseconds > maxAgeMilliseconds) {
            cache.remove(key);
            return null;
        }

        return cachedData.data;
    }

    /**
     * Removes cached data for one player.
     */
    public void remove(
            TierlistType tierlist,
            String username
    ) {
        cache.remove(createKey(tierlist, username));
    }

    /**
     * Clears the entire cache.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Creates a unique cache key.
     */
    private String createKey(
            TierlistType tierlist,
            String username
    ) {
        return tierlist.name()
                + ":"
                + username.toLowerCase();
    }

    private static class CachedPlayerData {

        private final PlayerTierData data;
        private final long timestamp;

        private CachedPlayerData(
                PlayerTierData data,
                long timestamp
        ) {
            this.data = data;
            this.timestamp = timestamp;
        }
    }
}
