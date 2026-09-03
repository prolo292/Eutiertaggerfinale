package com.xss1lent.universaltiertagger.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xss1lent.universaltiertagger.data.GameMode;
import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MCPVPProvider implements TierProvider {

    private static final String API_URL =
            "https://www.mcpvp.com/tiers/data";

    /*
     * MCPVP returns the entire player database.
     *
     * We cache it so multiple players using the mod
     * do not create a new request for every username.
     */
    private static final Map<String, PlayerTierData> CACHE =
            new ConcurrentHashMap<>();

    private static final long CACHE_DURATION =
            120_000L;

    private static volatile long lastUpdate = 0L;

    private static CompletableFuture<Void> loadingFuture;

    @Override
    public CompletableFuture<PlayerTierData> fetchPlayerTiers(
            String username
    ) {

        return loadDataIfNeeded()
                .thenApply(ignored -> {

                    PlayerTierData cached =
                            CACHE.get(username.toLowerCase());

                    if (cached == null) {
                        return new PlayerTierData(username);
                    }

                    return cached;
                });
    }

    private CompletableFuture<Void> loadDataIfNeeded() {

        long now = System.currentTimeMillis();

        if (!CACHE.isEmpty()
                && now - lastUpdate < CACHE_DURATION) {

            return CompletableFuture.completedFuture(null);
        }

        synchronized (MCPVPProvider.class) {

            now = System.currentTimeMillis();

            if (!CACHE.isEmpty()
                    && now - lastUpdate < CACHE_DURATION) {

                return CompletableFuture.completedFuture(null);
            }

            if (loadingFuture != null
                    && !loadingFuture.isDone()) {

                return loadingFuture;
            }

            loadingFuture = CompletableFuture.runAsync(
                    MCPVPProvider::downloadMCPVPData
            );

            return loadingFuture;
        }
    }

    private static void downloadMCPVPData() {

        try {

            HttpURLConnection connection =
                    (HttpURLConnection) URI.create(API_URL)
                            .toURL()
                            .openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);

            connection.setRequestProperty(
                    "User-Agent",
                    "UniversalTierTagger/1.0"
            );

            int responseCode =
                    connection.getResponseCode();

            if (responseCode != 200) {

                System.err.println(
                        "[Universal TierTagger] MCPVP returned HTTP "
                                + responseCode
                );

                return;
            }

            try (InputStreamReader reader =
                         new InputStreamReader(
                                 connection.getInputStream(),
                                 StandardCharsets.UTF_8
                         )) {

                JsonElement root =
                        JsonParser.parseReader(reader);

                if (!root.isJsonArray()) {
                    return;
                }

                JsonArray players =
                        root.getAsJsonArray();

                Map<String, PlayerTierData> newCache =
                        new ConcurrentHashMap<>();

                for (JsonElement element : players) {

                    if (!element.isJsonObject()) {
                        continue;
                    }

                    JsonObject player =
                            element.getAsJsonObject();

                    if (!player.has("name")
                            || player.get("name").isJsonNull()) {

                        continue;
                    }

                    /*
                     * Ignore retired players.
                     * We only want current MCPVP rankings.
                     */
                    if (player.has("retired")
                            && !player.get("retired").isJsonNull()
                            && player.get("retired").getAsBoolean()) {

                        continue;
                    }

                    String playerName =
                            player.get("name").getAsString();

                    PlayerTierData data =
                            new PlayerTierData(playerName);

                    if (player.has("kitRanks")
                            && player.get("kitRanks").isJsonObject()) {

                        JsonObject kitRanks =
                                player.getAsJsonObject("kitRanks");

                        for (Map.Entry<String, JsonElement> entry
                                : kitRanks.entrySet()) {

                            GameMode mode =
                                    getMCPVPMode(entry.getKey());

                            if (mode == null) {
                                continue;
                            }

                            JsonElement value =
                                    entry.getValue();

                            if (value == null
                                    || value.isJsonNull()
                                    || !value.isJsonPrimitive()) {

                                continue;
                            }

                            String tier =
                                    value.getAsString();

                            if (tier == null
                                    || tier.isBlank()
                                    || tier.equalsIgnoreCase("null")
                                    || tier.equalsIgnoreCase("-")) {

                                continue;
                            }

                            data.setTier(
                                    mode,
                                    tier.trim().toUpperCase()
                            );
                        }
                    }

                    if (data.hasAnyTier()) {

                        newCache.put(
                                playerName.toLowerCase(),
                                data
                        );
                    }
                }

                CACHE.clear();
                CACHE.putAll(newCache);

                lastUpdate = System.currentTimeMillis();

                System.out.println(
                        "[Universal TierTagger] Loaded "
                                + CACHE.size()
                                + " MCPVP player rankings."
                );
            }

            connection.disconnect();

        } catch (Exception exception) {

            System.err.println(
                    "[Universal TierTagger] Failed to download MCPVP data: "
                            + exception.getMessage()
            );
        }
    }

    /*
     * MCPVP mode mapping
     *
     * sword
     * shield
     * pot
     * early-game
     * end-game
     * mace
     * late-game
     * spear
     */
    private static GameMode getMCPVPMode(
            String modeName
    ) {

        if (modeName == null) {
            return null;
        }

        String normalized =
                modeName.toLowerCase()
                        .trim()
                        .replace("_", "-")
                        .replace(" ", "-");

        return switch (normalized) {

            case "sword" ->
                    GameMode.SWORD;

            case "shield" ->
                    GameMode.SHIELD;

            case "pot",
                 "potion" ->
                    GameMode.POT;

            case "early-game",
                 "earlygame" ->
                    GameMode.EARLY_GAME;

            case "end-game",
                 "endgame" ->
                    GameMode.END_GAME;

            case "mace" ->
                    GameMode.MACE;

            case "late-game",
                 "lategame" ->
                    GameMode.LATE_GAME;

            case "spear" ->
                    GameMode.SPEAR;

            default ->
                    null;
        };
    }

    @Override
    public TierlistType getTierlistType() {
        return TierlistType.MCPVP;
    }
}
