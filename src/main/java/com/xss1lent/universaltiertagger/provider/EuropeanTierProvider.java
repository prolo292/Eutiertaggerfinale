package com.xss1lent.universaltiertagger.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class EuropeanTierProvider implements TierProvider {

    private static final String API_URL =
            "http://fi12.bot-hosting.cloud:25344/player_rankings.json";

    @Override
    public CompletableFuture<PlayerTierData> fetchPlayerTiers(String username) {

        return CompletableFuture.supplyAsync(() -> {

            try {
                HttpURLConnection connection =
                        (HttpURLConnection) URI.create(API_URL)
                                .toURL()
                                .openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                try (InputStreamReader reader = new InputStreamReader(
                        connection.getInputStream(),
                        StandardCharsets.UTF_8
                )) {

                    JsonArray players =
                            JsonParser.parseReader(reader).getAsJsonArray();

                    for (JsonElement element : players) {

                        JsonObject player = element.getAsJsonObject();

                        if (!player.has("ign")) {
                            continue;
                        }

                        String ign = player.get("ign").getAsString();

                        if (!ign.equalsIgnoreCase(username)) {
                            continue;
                        }

                        PlayerTierData data =
                                new PlayerTierData(
                                        username,
                                        TierlistType.EUROPEAN
                                );

                        if (player.has("tiers")) {

                            JsonObject tiers =
                                    player.getAsJsonObject("tiers");

                            for (String mode : tiers.keySet()) {

                                String tier =
                                        tiers.get(mode).getAsString();

                                data.setTier(mode, tier);
                            }
                        }

                        return data;
                    }

                }

            } catch (Exception e) {
                System.err.println(
                        "[Universal TierTagger] Failed to fetch European tiers: "
                                + e.getMessage()
                );
            }

            return new PlayerTierData(
                    username,
                    TierlistType.EUROPEAN
            );
        });
    }

    @Override
    public TierlistType getTierlistType() {
        return TierlistType.EUROPEAN;
    }
}
