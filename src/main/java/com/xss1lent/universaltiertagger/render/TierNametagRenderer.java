package com.xss1lent.universaltiertagger.render;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.display.TierDisplayManager;
import com.xss1lent.universaltiertagger.display.TierTextFormatter;

public class TierNametagRenderer {

    public static String getPrimaryText(String username) {

        if (UniversalTierTaggerClient.CONFIG == null) {
            return "";
        }

        TierDisplayManager.DisplayTier tier =
                TierDisplayManager.getPrimaryTier(username);

        return TierTextFormatter.formatPrimary(tier);
    }

    public static String getSecondaryText(String username) {

        if (UniversalTierTaggerClient.CONFIG == null) {
            return "";
        }

        if (!UniversalTierTaggerClient.CONFIG.showSecondaryTierlist) {
            return "";
        }

        TierDisplayManager.DisplayTier tier =
                TierDisplayManager.getSecondaryTier(username);

        return TierTextFormatter.formatSecondary(tier);
    }
}
