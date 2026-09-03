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

            // Check players every 2 seconds (40 ticks)
            tickCounter++;

            if (tickCounter < 40) {
                return;
            }

            tickCounter = 0;

            client.level.players().forEach(player -> {

                if (player == client.player) {
                    return;
                }

                String username =
                        player.getGameProfile().getName();

                if (username == null || username.isBlank()) {
                    return;
                }

                loadPlayerIfNeeded(username);
            });
        });
    }

    private static void loadPlayerIfNeeded(String username) {

        if (LOADING_PLAYERS.contains(username.toLowerCase())) {
            return;
        }

        // Check if at least one tierlist is already cached
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

        // All data already cached
        if (european != null
                && mctiers != null
                && mcpvp != null) {
            return;
        }

        String loadingKey = username.toLowerCase();

        if (!LOADING_PLAYERS.add(loadingKey)) {
            return;
        }

        // Fetch all three tierlists
        UniversalTierTaggerClient.PROVIDERS
                .fetchPlayerTiers(
                        TierlistType.EUROPEAN,
                        username
                );

        UniversalTierTaggerClient.PROVIDERS
                .fetchPlayerTiers(
                        TierlistType.MCTIERS,
                        username
                );

        UniversalTierTaggerClient.PROVIDERS
                .fetchPlayerTiers(
                        TierlistType.MCPVP,
                        username
                )
                .whenComplete((result, throwable) -> {
                    LOADING_PLAYERS.remove(loadingKey);
                });
    }
}
