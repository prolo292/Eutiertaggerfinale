package com.xss1lent.universaltiertagger;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniversalTierTaggerClient implements ClientModInitializer {

    public static final String MOD_ID = "universal_tiertagger";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Universal TierTagger has been initialized!");
    }
}
