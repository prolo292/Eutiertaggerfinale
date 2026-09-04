package com.xss1lent.universaltiertagger.display;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.render.ModeIcon;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;

public class TierComponentFormatter {

    private static final Identifier ICON_FONT_ID =
            Identifier.fromNamespaceAndPath(
                    "universal_tiertagger",
                    "icons"
            );

    private static final FontDescription.Resource ICON_FONT =
            new FontDescription.Resource(ICON_FONT_ID);

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

        // Tierlist name
        if (UniversalTierTaggerClient.CONFIG.showTierlistLogo) {

            result = result.copy().append(
                    Component.literal(
                            "[" + getTierlistShortName(
                                    displayTier.tierlist().name()
                            ) + "] "
                    )
            );
        }

        // Mode icon
        if (UniversalTierTaggerClient.CONFIG.showModeIcon) {

            String icon =
                    ModeIcon.getIcon(displayTier.mode());

            Component iconComponent =
                    Component.literal(icon)
                            .setStyle(
                                    Style.EMPTY.withFont(ICON_FONT)
                            );

            result = result.copy()
                    .append(iconComponent)
                    .append(Component.literal(" "));
        }

        // Tier text with its configured color
        String tier = displayTier.tier();

        String hexColor =
                TierTextFormatter.getTierColor(tier);

        TextColor color =
                TextColor.parseColor(hexColor)
                        .result()
                        .orElse(
                                TextColor.fromRgb(0xFFFFFF)
                        );

        Component tierComponent =
                Component.literal(tier)
                        .setStyle(
                                Style.EMPTY.withColor(color)
                        );

        return result.copy()
                .append(tierComponent);
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
