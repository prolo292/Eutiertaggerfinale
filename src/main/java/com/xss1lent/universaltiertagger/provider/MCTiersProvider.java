package com.xss1lent.universaltiertagger.provider;

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

public class MCTiersProvider implements TierProvider {

    private static final String API_BASE =
            "https://mctiers.com/api/v2/profile/by-name/";

    @Override
    public CompletableFuture<PlayerTierData> fetchPlayerTiers(String username) {

        return CompletableFuture.supplyAsync(() -> {

            PlayerTierData data = new PlayerTierData(username);

            try {
                String url = API_BASE + username;

                HttpURLConnection connection =
                        (HttpURLConnection) URI.create(url)
                                .toURL()
                                .openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                try (InputStreamReader reader = new InputStreamReader(
                        connection.getInputStream(),
                        StandardCharsets.UTF_8
                )) {

                    JsonElement root =
                            JsonParser.parseReader(reader);

                    if (!root.isJsonObject()) {
                        return data;
                    }

                    JsonObject profile = root.getAsJsonObject();

                    // Try to locate tier/ranking objects in the response
                    for (String possibleKey : new String[]{
                            "tiers",
                            "rankings",
                            "ladders",
                            "placements"
                    }) {

                        if (!profile.has(possibleKey)
                                || !profile.get(possibleKey).isJsonObject()) {
                            continue;
                        }

                        JsonObject tierObject =
                                profile.getAsJsonObject(possibleKey);

                        for (Map.Entry<String, JsonElement> entry
                                : tierObject.entrySet()) {

                            GameMode mode =
                                    GameMode.fromString(entry.getKey());

                            if (mode == null) {
                                continue;
                            }

                            JsonElement value = entry.getValue();

                            String tier = null;

                            if (value.isJsonPrimitive()) {
                                tier = value.getAsString();

                            } else if (value.isJsonObject()) {

                                JsonObject tierData =
                                        value.getAsJsonObject();

                                if (tierData.has("tier")) {
                                    tier = tierData
                                            .get("tier")
                                            .getAsString();
                                }
                            }

                            if (tier != null
                                    && !tier.isBlank()
                                    && !tier.equalsIgnoreCase("null")) {

                                data.setTier(mode, tier);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                System.err.println(
                        "[Universal TierTagger] Failed to fetch MCTiers data: "
                                + e.getMessage()
                );
            }

            return data;
        });
    }

    @Override
    public TierlistType getTierlistType() {
        return TierlistType.MCTIERS;
    }
}
