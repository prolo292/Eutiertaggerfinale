package com.xss1lent.universaltiertagger.display;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;

public class TierTextFormatter {

    public static String getPrimaryText(String username) {

        TierDisplayManager.DisplayTier tier =
                TierDisplayManager.getPrimaryTier(username);

        if (tier == null) {
            return "";
        }

        return "[" +
                shortName(tier.tierlist().name()) +
                " " +
                tier.mode().getDisplayName() +
                " " +
                tier.tier() +
                "]";
    }

    public static String getSecondaryText(String username) {

        TierDisplayManager.DisplayTier tier =
                TierDisplayManager.getSecondaryTier(username);

        if (tier == null) {
            return "";
        }

        return "[" +
                shortName(tier.tierlist().name()) +
                " " +
                tier.mode().getDisplayName() +
                " " +
                tier.tier() +
                "]";
    }

    public static String formatName(String username) {

        if (UniversalTierTaggerClient.CONFIG == null) {
            return username;
        }

        if (!UniversalTierTaggerClient.CONFIG.showPrimary) {
            return username;
        }

        String tier = getPrimaryText(username);

        if (tier.isEmpty()) {
            return username;
        }

        return username + " " + tier;
    }

    private static String shortName(String name) {

        return switch (name) {
            case "EUROPEAN" -> "EU";
            case "MCTIERS" -> "MCT";
            case "MCPVP" -> "MCPVP";
            default -> name;
        };
    }
}
