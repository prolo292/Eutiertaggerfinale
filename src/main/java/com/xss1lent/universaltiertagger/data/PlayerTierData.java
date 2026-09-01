package com.xss1lent.universaltiertagger.data;

import java.util.EnumMap;
import java.util.Map;

public class PlayerTierData {

    private String username;
    private final Map<GameMode, String> tiers;

    public PlayerTierData(String username) {
        this.username = username;
        this.tiers = new EnumMap<>(GameMode.class);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTier(GameMode mode, String tier) {
        if (mode == null || tier == null || tier.isBlank()) {
            return;
        }

        tiers.put(mode, tier.toUpperCase());
    }

    public String getTier(GameMode mode) {
        if (mode == null) {
            return null;
        }

        return tiers.get(mode);
    }

    public Map<GameMode, String> getAllTiers() {
        return new EnumMap<>(tiers);
    }

    public boolean hasTier(GameMode mode) {
        return mode != null && tiers.containsKey(mode);
    }

    public boolean hasAnyTier() {
        return !tiers.isEmpty();
    }

    public void clear() {
        tiers.clear();
    }

    /**
     * Returns the best tier using the configured mode priority.
     */
    public String getHighestTier() {
        String bestTier = null;
        int bestScore = Integer.MAX_VALUE;

        for (String tier : tiers.values()) {
            int score = getTierScore(tier);

            if (score < bestScore) {
                bestScore = score;
                bestTier = tier;
            }
        }

        return bestTier;
    }

    /**
     * Returns the game mode associated with the best tier.
     */
    public GameMode getHighestTierMode() {
        GameMode bestMode = null;
        int bestScore = Integer.MAX_VALUE;

        for (Map.Entry<GameMode, String> entry : tiers.entrySet()) {
            int score = getTierScore(entry.getValue());

            if (score < bestScore) {
                bestScore = score;
                bestMode = entry.getKey();
            }
        }

        return bestMode;
    }

    /**
     * Tier ranking:
     * HT1 is the best tier.
     * LT5 is lower.
     */
    private int getTierScore(String tier) {
        if (tier == null) {
            return Integer.MAX_VALUE;
        }

        return switch (tier.toUpperCase()) {
            case "HT1" -> 1;
            case "LT1" -> 2;
            case "HT2" -> 3;
            case "LT2" -> 4;
            case "HT3" -> 5;
            case "LT3" -> 6;
            case "HT4" -> 7;
            case "LT4" -> 8;
            case "HT5" -> 9;
            case "LT5" -> 10;
            default -> Integer.MAX_VALUE;
        };
    }
}
