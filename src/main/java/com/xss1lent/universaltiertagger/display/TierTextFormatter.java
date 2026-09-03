package com.xss1lent.universaltiertagger.display;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;

public class TierTextFormatter {

    /**
     * Text displayed next to the player's name.
     *
     * Example:
     * [EU] Crystal LT2
     */
    public static String formatPrimary(
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

            /*
             * Temporary text representation.
             *
             * Later this will be replaced by
             * actual Minecraft textures/icons.
             */
            builder.append("[")
                    .append(displayTier.mode().getDisplayName())
                    .append("] ");
        }

        builder.append(displayTier.tier());

        return builder.toString();
    }

    /**
     * Text displayed above the player's nametag.
     */
    public static String formatSecondary(
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

    private static String getTierlistShortName(
            String tierlist
    ) {

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
