package com.xss1lent.universaltiertagger;

import com.xss1lent.universaltiertagger.cache.TierCache;
import com.xss1lent.universaltiertagger.command.TierCommands;
import com.xss1lent.universaltiertagger.config.TierTaggerConfig;
import com.xss1lent.universaltiertagger.keybind.KeyBindings;
import com.xss1lent.universaltiertagger.provider.TierProviderManager;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniversalTierTaggerClient implements ClientModInitializer {

    public static final String MOD_ID = "universal_tiertagger";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static TierTaggerConfig CONFIG;
    public static TierCache CACHE;
    public static TierProviderManager PROVIDERS;

    @Override
    public void onInitializeClient() {

        LOGGER.info("Initializing Universal TierTagger...");

        CONFIG = TierTaggerConfig.load();
        CACHE = new TierCache();
        PROVIDERS = new TierProviderManager();

        KeyBindings.register();

        TierCommands.register();

        LOGGER.info("Universal TierTagger initialized!");
    }
}
