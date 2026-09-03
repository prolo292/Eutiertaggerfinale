package com.xss1lent.universaltiertagger.display;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class TierComponentFormatter {

    public static Component formatPrimary(
            TierDisplayManager.DisplayTier displayTier
    ) {
        return format(displayTier);
    }

    public static Component formatSecondary(
            TierDisplayManager.DisplayTier displayTier
    ) {
        return format(displayTier);
    }

    private static Component format(
            TierDisplayManager.DisplayTier displayTier
    ) {

        if (displayTier == null) {
            return Component.empty();
        }

        Component result = Component.empty();

        if (UniversalTierTaggerClient.CONFIG.showTierlistLogo) {

            result = result.copy().append(
                    Component.literal(
                            "[" + getTierlistShortName(
                                    displayTier.tierlist().name()
                            ) + "] "
                    )
            );
        }

        if (UniversalTierTaggerClient.CONFIG.showModeIcon) {

            result = result.copy().append(
                    Component.literal(
                            "[" + displayTier.mode().getDisplayName()
                                    + "] "
                    )
            );
        }

        String tier = displayTier.tier();

        String hexColor =
                TierTextFormatter.getTierColor(tier);

        TextColor color =
                TextColor.parseColor(hexColor)
                        .result()
                        .orElse(TextColor.fromRgb(0xFFFFFF));

        Component tierComponent =
                Component.literal(tier)
                        .setStyle(
                                Style.EMPTY.withColor(color)
                        );

        return result.copy().append(tierComponent);
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
