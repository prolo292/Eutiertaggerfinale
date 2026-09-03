package com.xss1lent.universaltiertagger.render;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.display.TierDisplayManager;
import com.xss1lent.universaltiertagger.display.TierTextFormatter;

public class TierTabRenderer {

    /**
     * Gets the tier text that should appear in the TAB list.
     *
     * TAB only shows the primary tierlist.
     */
    public static String getTabText(String username) {

        if (UniversalTierTaggerClient.CONFIG == null) {
            return "";
        }

        if (!UniversalTierTaggerClient.CONFIG.showInTab) {
            return "";
        }

        TierDisplayManager.DisplayTier tier =
                TierDisplayManager.getPrimaryTier(username);

        if (tier == null) {
            return "";
        }

        return TierTextFormatter.formatPrimary(tier);
    }
}
