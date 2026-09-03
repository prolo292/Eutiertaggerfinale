package com.xss1lent.universaltiertagger.render;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.display.TierTextFormatter;

public class TierNametagRenderer {

    public static String getSecondaryNametag(String username) {

        if (UniversalTierTaggerClient.CONFIG == null) {
            return "";
        }

        if (!UniversalTierTaggerClient.CONFIG.showSecondary) {
            return "";
        }

        return TierTextFormatter.getSecondaryText(username);
    }
}
