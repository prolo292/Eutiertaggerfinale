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

        StringBuilder builder = new StringBuilder();

        if (UniversalTierTaggerClient.CONFIG.showTierlistLogo) {

            builder.append("[")
                    .append(getTierlistShortName(
                            displayTier.tierlist().name()
                    ))
                    .append("] ");
        }

        if (UniversalTierTaggerClient.CONFIG.showModeIcon) {

            builder.append("[")
                    .append(displayTier.mode().getDisplayName())
                    .append("] ");
        }

        builder.append(displayTier.tier());

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

    private static String getTierlistShortName(String tierlist) {

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
