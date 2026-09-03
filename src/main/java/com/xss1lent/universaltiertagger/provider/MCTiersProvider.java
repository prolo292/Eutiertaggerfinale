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

                connection.setRequestProperty(
                        "User-Agent",
                        "UniversalTierTagger/1.0"
                );

                int responseCode = connection.getResponseCode();

                if (responseCode != 200) {
                    return data;
                }

                try (InputStreamReader reader =
                             new InputStreamReader(
                                     connection.getInputStream(),
                                     StandardCharsets.UTF_8
                             )) {

                    JsonElement root =
                            JsonParser.parseReader(reader);

                    if (!root.isJsonObject()) {
                        return data;
                    }

                    JsonObject profile =
                            root.getAsJsonObject();

                    parsePossibleTierObject(
                            profile,
                            data
                    );

                    String[] possibleKeys = {
                            "tiers",
                            "rankings",
                            "ladders",
                            "placements",
                            "gameModes",
                            "gamemodes"
                    };

                    for (String key : possibleKeys) {

                        if (!profile.has(key)) {
                            continue;
                        }

                        JsonElement element =
                                profile.get(key);

                        if (element.isJsonObject()) {

                            parsePossibleTierObject(
                                    element.getAsJsonObject(),
                                    data
                            );
                        }
                    }
                }

                connection.disconnect();

            } catch (Exception exception) {

                System.err.println(
                        "[Universal TierTagger] "
                                + "Failed to fetch MCTiers data for "
                                + username
                                + ": "
                                + exception.getMessage()
                );
            }

            return data;
        });
    }

    private void parsePossibleTierObject(
            JsonObject object,
            PlayerTierData data
    ) {

        for (Map.Entry<String, JsonElement> entry
                : object.entrySet()) {

            GameMode mode =
                    getMCTiersMode(entry.getKey());

            if (mode == null) {
                continue;
            }

            String tier =
                    extractTier(entry.getValue());

            if (tier == null
                    || tier.isBlank()
                    || tier.equalsIgnoreCase("null")
                    || tier.equalsIgnoreCase("unranked")) {

                continue;
            }

            data.setTier(
                    mode,
                    normalizeTier(tier)
            );
        }
    }

    private GameMode getMCTiersMode(String value) {

        if (value == null) {
            return null;
        }

        String mode = value
                .toLowerCase()
                .replace("-", "")
                .replace("_", "")
                .replace(" ", "");

        return switch (mode) {

            case "sword" -> GameMode.SWORD;

            case "axe" -> GameMode.AXE;

            case "pot",
                 "potion" -> GameMode.POT;

            case "nethop",
                 "nethpot",
                 "netheritepot" -> GameMode.NETHOP;

            case "uhc" -> GameMode.UHC;

            case "smp" -> GameMode.SMP;

            case "vanilla" -> GameMode.VANILLA;

            case "mace" -> GameMode.MACE;

            default -> null;
        };
    }

    private String extractTier(JsonElement value) {

        if (value == null
                || value.isJsonNull()) {

            return null;
        }

        if (value.isJsonPrimitive()) {

            return value.getAsString();
        }

        if (!value.isJsonObject()) {

            return null;
        }

        JsonObject object =
                value.getAsJsonObject();

        String[] possibleTierKeys = {
                "tier",
                "rank",
                "currentTier",
                "current_tier",
                "placement"
        };

        for (String key : possibleTierKeys) {

            if (object.has(key)
                    && !object.get(key).isJsonNull()) {

                JsonElement tierElement =
                        object.get(key);

                if (tierElement.isJsonPrimitive()) {
                    return tierElement.getAsString();
                }
            }
        }

        return null;
    }

    private String normalizeTier(String tier) {

        if (tier == null) {
            return null;
        }

        String normalized =
                tier.trim().toUpperCase();

        /*
         * MCTiers may return:
         *
         * 4
         * HT4
         * LT4
         *
         * We preserve the exact format when possible.
         */

        return normalized;
    }

    @Override
    public TierlistType getTierlistType() {
        return TierlistType.MCTIERS;
    }
}
