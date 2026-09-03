package com.xss1lent.universaltiertagger.tracker;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerTierTracker {

    private static final Set<String> LOADING_PLAYERS =
            ConcurrentHashMap.newKeySet();

    private static int tickCounter = 0;

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.level == null || client.player == null) {
                return;
            }

            // Check players every 2 seconds
            tickCounter++;

            if (tickCounter < 40) {
                return;
            }

            tickCounter = 0;

            client.level.players().forEach(player -> {

                // Don't fetch our own player
                if (player == client.player) {
                    return;
                }

                String username = player.getGameProfile().name();

                if (username == null || username.isBlank()) {
                    return;
                }

                loadPlayerIfNeeded(username);
            });
        });
    }

    private static void loadPlayerIfNeeded(String username) {

        if (UniversalTierTaggerClient.CACHE == null
                || UniversalTierTaggerClient.PROVIDERS == null) {
            return;
        }

        String loadingKey = username.toLowerCase();

        // Already loading
        if (LOADING_PLAYERS.contains(loadingKey)) {
            return;
        }

        PlayerTierData european =
                UniversalTierTaggerClient.CACHE.get(
                        TierlistType.EUROPEAN,
                        username,
                        300
                );

        PlayerTierData mctiers =
                UniversalTierTaggerClient.CACHE.get(
                        TierlistType.MCTIERS,
                        username,
                        300
                );

        PlayerTierData mcpvp =
                UniversalTierTaggerClient.CACHE.get(
                        TierlistType.MCPVP,
                        username,
                        300
                );

        // Everything already cached
        if (european != null
                && mctiers != null
                && mcpvp != null) {
            return;
        }

        if (!LOADING_PLAYERS.add(loadingKey)) {
            return;
        }

        // Fetch all tierlists
        var europeanFuture =
                UniversalTierTaggerClient.PROVIDERS.fetchPlayerTiers(
                        TierlistType.EUROPEAN,
                        username
                );

        var mctiersFuture =
                UniversalTierTaggerClient.PROVIDERS.fetchPlayerTiers(
                        TierlistType.MCTIERS,
                        username
                );

        var mcpvpFuture =
                UniversalTierTaggerClient.PROVIDERS.fetchPlayerTiers(
                        TierlistType.MCPVP,
                        username
                );

  java.util.concurrent.CompletableFuture.allOf(
        europeanFuture,
        mctiersFuture,
        mcpvpFuture
).whenComplete((result, throwable) -> {

    try {

        if (throwable == null) {

            PlayerTierData europeanData = europeanFuture.join();
            PlayerTierData mctiersData = mctiersFuture.join();
            PlayerTierData mcpvpData = mcpvpFuture.join();

            UniversalTierTaggerClient.CACHE.put(
                    TierlistType.EUROPEAN,
                    username,
                    europeanData
            );

            UniversalTierTaggerClient.CACHE.put(
                    TierlistType.MCTIERS,
                    username,
                    mctiersData
            );

            UniversalTierTaggerClient.CACHE.put(
                    TierlistType.MCPVP,
                    username,
                    mcpvpData
            );
        }

    } catch (Exception exception) {

        UniversalTierTaggerClient.LOGGER.warn(
                "Failed to cache tiers for {}",
                username,
                exception
        );

    } finally {

        LOADING_PLAYERS.remove(loadingKey);
    }

    if (throwable != null) {

        UniversalTierTaggerClient.LOGGER.warn(
                "Failed to load tiers for {}",
                username,
                throwable
        );
    }
});
