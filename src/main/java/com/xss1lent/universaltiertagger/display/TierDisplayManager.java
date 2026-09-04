package com.xss1lent.universaltiertagger.display;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.data.GameMode;
import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;

public class TierDisplayManager {

    public static DisplayTier getPrimaryTier(String username) {

        if (UniversalTierTaggerClient.CONFIG == null) {
            return null;
        }

        return getTierForTierlist(
                UniversalTierTaggerClient.CONFIG.primaryTierlist,
                username
        );
    }

    public static DisplayTier getSecondaryTier(String username) {

        if (UniversalTierTaggerClient.CONFIG == null
                || !UniversalTierTaggerClient.CONFIG.showSecondaryTierlist) {
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
                || tierlistName.isBlank()
                || username == null
                || username.isBlank()
                || UniversalTierTaggerClient.CACHE == null
                || UniversalTierTaggerClient.CONFIG == null) {
            return null;
        }

        TierlistType type;

        try {
            type = TierlistType.valueOf(
                    tierlistName.trim().toUpperCase()
            );
        } catch (IllegalArgumentException exception) {
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

        if ("HIGHEST".equalsIgnoreCase(
                UniversalTierTaggerClient.CONFIG.displayType
        )) {

            String tier = data.getHighestTier();
            GameMode mode = data.getHighestTierMode();

            if (!isValidTier(tier) || mode == null) {
                return null;
            }

            return new DisplayTier(type, mode, tier);
        }

        GameMode mode = GameMode.fromString(
                UniversalTierTaggerClient.CONFIG.specificMode
        );

        if (mode == null) {
            return null;
        }

        String tier = data.getTier(mode);

        if (!isValidTier(tier)) {
            return null;
        }

        return new DisplayTier(type, mode, tier);
    }

    private static boolean isValidTier(String tier) {

        if (tier == null || tier.isBlank()) {
            return false;
        }

        if ("UNRANKED".equalsIgnoreCase(tier)
                && !UniversalTierTaggerClient.CONFIG.showUnranked) {
            return false;
        }

        return true;
    }

    public record DisplayTier(
            TierlistType tierlist,
            GameMode mode,
            String tier
    ) {
    }
}
