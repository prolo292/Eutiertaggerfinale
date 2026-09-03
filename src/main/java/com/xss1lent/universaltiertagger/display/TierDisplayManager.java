package com.xss1lent.universaltiertagger.display;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.data.GameMode;
import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;

public class TierDisplayManager {

    public static DisplayTier getPrimaryTier(String username) {
        return getTierForTierlist(
                UniversalTierTaggerClient.CONFIG.primaryTierlist,
                username
        );
    }

    public static DisplayTier getSecondaryTier(String username) {

        if (!UniversalTierTaggerClient.CONFIG.showSecondaryTierlist) {
            return null;
        }

        return getTierForTierlist(
                UniversalTierTaggerClient.CONFIG.secondaryTierlist,
                username
        );
    }

    private static DisplayTier getTierForTierlist(
            String tierlistName,
            String username
    ) {

        if (tierlistName == null
                || username == null
                || UniversalTierTaggerClient.CACHE == null) {
            return null;
        }

        TierlistType type;

        try {
            type = TierlistType.valueOf(
                    tierlistName.toUpperCase()
            );
        } catch (Exception exception) {
            return null;
        }

        PlayerTierData data =
                UniversalTierTaggerClient.CACHE.get(
                        type,
                        username,
                        300
                );

        if (data == null || !data.hasAnyTier()) {
            return null;
        }

        String displayType =
                UniversalTierTaggerClient.CONFIG.displayType;

        if ("HIGHEST".equalsIgnoreCase(displayType)) {

            String tier = data.getHighestTier();
            GameMode mode = data.getHighestTierMode();

            if (tier == null || mode == null) {
                return null;
            }

            return new DisplayTier(
                    type,
                    mode,
                    tier
            );
        }

        GameMode mode = GameMode.fromString(
                UniversalTierTaggerClient.CONFIG.specificMode
        );

        String tier = data.getTier(mode);

        if (tier == null) {
            return null;
        }

        return new DisplayTier(
                type,
                mode,
                tier
        );
    }

    public record DisplayTier(
            TierlistType tierlist,
            GameMode mode,
            String tier
    ) {
    }
}
