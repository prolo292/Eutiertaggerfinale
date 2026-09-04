package com.xss1lent.universaltiertagger.display;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;

public class TierTextFormatter {

    public static String formatPrimary(
            TierDisplayManager.DisplayTier displayTier
    ) {
        return format(displayTier);
    }

    public static String formatSecondary(
            TierDisplayManager.DisplayTier displayTier
    ) {
        return format(displayTier);
    }

    private static String format(
            TierDisplayManager.DisplayTier displayTier
    ) {

        if (displayTier == null) {
            return "";
        }

        String tier = displayTier.tier();

        if (tier == null || tier.isBlank()) {
            return "";
        }

        // Do not display unranked players unless enabled.
        if ("UNRANKED".equalsIgnoreCase(tier)
                && !UniversalTierTaggerClient.CONFIG.showUnranked) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        /*
         * Tierlist identifier.
         *
         * The actual logo/icon rendering is handled separately by
         * TierComponentFormatter. This text formatter only provides
         * fallback text.
         */
        if (UniversalTierTaggerClient.CONFIG.showTierlistLogo) {

            builder.append("[")
                    .append(
                            getTierlistShortName(
                                    displayTier.tierlist().name()
                            )
                    )
                    .append("] ");
        }

        /*
         * Mode icon is rendered separately.
         * Do NOT put [Sword], [Axe], etc. here.
         */
        builder.append(tier);

        return builder.toString();
    }

    public static String getTierColor(String tier) {

        if (UniversalTierTaggerClient.CONFIG == null
                || UniversalTierTaggerClient.CONFIG.tierColors == null
                || tier == null) {
            return "#FFFFFF";
        }

        return UniversalTierTaggerClient.CONFIG.tierColors.getOrDefault(
                tier.toUpperCase(),
                "#FFFFFF"
        );
    }

    public static String getTierlistShortName(String tierlist) {

        if (tierlist == null) {
            return "UNKNOWN";
        }

        return switch (tierlist.toUpperCase()) {
            case "EUROPEAN" -> "EU";
            case "MCTIERS" -> "MCT";
            case "MCPVP" -> "MCPVP";
            default -> tierlist;
        };
    }
}
