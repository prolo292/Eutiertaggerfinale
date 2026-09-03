package com.xss1lent.universaltiertagger.display;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.data.GameMode;
import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;

public class TierDisplayManager {

    /**
     * Gets the tier that should appear next to the player's name.
     */
    public static DisplayTier getPrimaryTier(String username) {

        return getTier(
                username,
                UniversalTierTaggerClient.CONFIG.primaryTierlist,
                UniversalTierTaggerClient.CONFIG.primaryDisplayType,
                UniversalTierTaggerClient.CONFIG.primarySpecificMode
        );
    }

    /**
     * Gets the tier that should appear above the player's head.
     */
    public static DisplayTier getSecondaryTier(String username) {

        return getTier(
                username,
                UniversalTierTaggerClient.CONFIG.secondaryTierlist,
                UniversalTierTaggerClient.CONFIG.secondaryDisplayType,
                UniversalTierTaggerClient.CONFIG.secondarySpecificMode
        );
    }

    private static DisplayTier getTier(
            String username,
            String tierlistName,
            String displayType,
            String specificMode
    ) {

        if (username == null
                || UniversalTierTaggerClient.CONFIG == null
                || UniversalTierTaggerClient.CACHE == null) {
            return null;
        }

        TierlistType tierlist;

        try {
            tierlist = TierlistType.valueOf(
                    tierlistName.toUpperCase()
            );
        } catch (Exception exception) {
            return null;
        }

        PlayerTierData data =
                UniversalTierTaggerClient.CACHE.get(
                        tierlist,
                        username,
                        300
                );

        if (data == null || !data.hasAnyTier()) {
            return null;
        }

        GameMode mode;
        String tier;

        if ("HIGHEST".equalsIgnoreCase(displayType)) {

            mode = data.getHighestTierMode();
            tier = data.getHighestTier();

        } else {

            mode = GameMode.fromString(specificMode);
            tier = data.getTier(mode);
        }

        if (tier == null || tier.isBlank()) {

            if (!UniversalTierTaggerClient.CONFIG.showUnranked) {
                return null;
            }

            tier = "UNRANKED";
        }

        return new DisplayTier(
                tierlist,
                mode,
                tier
        );
    }

    /**
     * Represents one tier ready for rendering.
     */
    public record DisplayTier(
            TierlistType tierlist,
            GameMode mode,
            String tier
    ) {
    }
}
