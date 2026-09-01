package com.xss1lent.universaltiertagger.provider;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;

import java.util.concurrent.CompletableFuture;

public class EuropeanTierProvider implements TierProvider {

    public static final String BASE_URL =
            "http://fi12.bot-hosting.cloud:25344";

    @Override
    public TierlistType getTierlistType() {
        return TierlistType.EUROPEAN;
    }

    @Override
    public CompletableFuture<PlayerTierData> fetchPlayerTiers(String username) {

        return CompletableFuture.supplyAsync(() -> {

            UniversalTierTaggerClient.LOGGER.info(
                    "Fetching European tiers for {}",
                    username
            );

            /*
             * The exact API endpoint and JSON format will be implemented
             * once the European Tierlist API structure is confirmed.
             */

            return new PlayerTierData(username);
        });
    }
}
