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

public class MCPVPProvider implements TierProvider {

    private static final String API_URL =
            "https://www.mcpvp.com/tiers/data";

    @Override
    public CompletableFuture<PlayerTierData> fetchPlayerTiers(String username) {

        return CompletableFuture.supplyAsync(() -> {

            PlayerTierData data = new PlayerTierData(username);

            try {
                HttpURLConnection connection =
                        (HttpURLConnection) URI.create(API_URL)
                                .toURL()
                                .openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(10000);

                try (InputStreamReader reader = new InputStreamReader(
                        connection.getInputStream(),
                        StandardCharsets.UTF_8
                )) {

                    JsonElement root =
                            JsonParser.parseReader(reader);

                    if (!root.isJsonArray()) {
                        return data;
                    }

                    JsonArray players =
                            root.getAsJsonArray();

                    for (JsonElement element : players) {

                        if (!element.isJsonObject()) {
                            continue;
                        }

                        JsonObject player =
                                element.getAsJsonObject();

                        if (!player.has("name")) {
                            continue;
                        }

                        String playerName =
                                player.get("name").getAsString();

                        if (!playerName.equalsIgnoreCase(username)) {
                            continue;
                        }

                        // Ignore retired players
                        if (player.has("retired")
                                && player.get("retired").getAsBoolean()) {
                            return data;
                        }

                        if (!player.has("kitRanks")
                                || !player.get("kitRanks").isJsonObject()) {
                            return data;
                        }

                        JsonObject kitRanks =
                                player.getAsJsonObject("kitRanks");

                        for (Map.Entry<String, JsonElement> entry
                                : kitRanks.entrySet()) {

                            String modeName =
                                    entry.getKey();

                            GameMode mode =
                                    GameMode.fromString(modeName);

                            if (mode == null) {
                                continue;
                            }

                            if (!entry.getValue().isJsonPrimitive()) {
                                continue;
                            }

                            String tier =
                                    entry.getValue().getAsString();

                            if (tier == null
                                    || tier.isBlank()
                                    || tier.equalsIgnoreCase("null")) {
                                continue;
                            }

                            data.setTier(mode, tier);
                        }

                        return data;
                    }
                }

            } catch (Exception e) {
                System.err.println(
                        "[Universal TierTagger] Failed to fetch MCPVP data: "
                                + e.getMessage()
                );
            }

            return data;
        });
    }

    @Override
    public TierlistType getTierlistType() {
        return TierlistType.MCPVP;
    }
}
