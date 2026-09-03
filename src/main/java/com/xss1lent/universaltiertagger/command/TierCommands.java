package com.xss1lent.universaltiertagger.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.data.GameMode;
import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TierCommands {

    public static void register() {

        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("eutiers")

                                .then(ClientCommandManager.argument(
                                        "username",
                                        StringArgumentType.word()
                                ).executes(context -> {

                                    String username =
                                            StringArgumentType.getString(
                                                    context,
                                                    "username"
                                            );

                                    fetchAndDisplay(username);

                                    return 1;
                                }))
                )
        );
    }

    private static void fetchAndDisplay(String username) {

        if (UniversalTierTaggerClient.PROVIDERS == null) {
            return;
        }

        var client = net.minecraft.client.Minecraft.getInstance();

        if (client.player != null) {
            client.player.displayClientMessage(
                    Component.literal("[EU Tiers] Searching tiers for " + username + "..."),
                    false
            );
        }

        CompletableFuture<PlayerTierData> european =
                UniversalTierTaggerClient.PROVIDERS.fetchPlayerTiers(
                        TierlistType.EUROPEAN,
                        username
                );

        CompletableFuture<PlayerTierData> mctiers =
                UniversalTierTaggerClient.PROVIDERS.fetchPlayerTiers(
                        TierlistType.MCTIERS,
                        username
                );

        CompletableFuture<PlayerTierData> mcpvp =
                UniversalTierTaggerClient.PROVIDERS.fetchPlayerTiers(
                        TierlistType.MCPVP,
                        username
                );

        CompletableFuture.allOf(european, mctiers, mcpvp)
                .thenRun(() -> {

                    try {

                        PlayerTierData euData = european.join();
                        PlayerTierData mcTiersData = mctiers.join();
                        PlayerTierData mcpvpData = mcpvp.join();

                        client.execute(() -> {

                            if (client.player == null) {
                                return;
                            }

                            client.player.displayClientMessage(
                                    Component.literal(""),
                                    false
                            );

                            client.player.displayClientMessage(
                                    Component.literal("================================"),
                                    false
                            );

                            client.player.displayClientMessage(
                                    Component.literal("TIERS FOR " + username),
                                    false
                            );

                            displayTierlist(
                                    "EUROPEAN TIERS",
                                    euData
                            );

                            displayTierlist(
                                    "MC TIERS",
                                    mcTiersData
                            );

                            displayTierlist(
                                    "MCPVP BETA",
                                    mcpvpData
                            );

                            client.player.displayClientMessage(
                                    Component.literal("================================"),
                                    false
                            );
                        });

                    } catch (Exception exception) {

                        UniversalTierTaggerClient.LOGGER.error(
                                "Failed to fetch player tiers",
                                exception
                        );
                    }
                });
    }

    private static void displayTierlist(
            String title,
            PlayerTierData data
    ) {

        var client = net.minecraft.client.Minecraft.getInstance();

        if (client.player == null) {
            return;
        }

        client.player.displayClientMessage(
                Component.literal(""),
                false
        );

        client.player.displayClientMessage(
                Component.literal(title),
                false
        );

        if (data == null || !data.hasAnyTier()) {

            client.player.displayClientMessage(
                    Component.literal("  No tiers found."),
                    false
            );

            return;
        }

        for (Map.Entry<GameMode, String> entry
                : data.getAllTiers().entrySet()) {

            GameMode mode = entry.getKey();
            String tier = entry.getValue();

            client.player.displayClientMessage(
                    Component.literal(
                            "  • "
                                    + mode.getDisplayName()
                                    + ": "
                                    + tier
                    ),
                    false
            );
        }
    }
}
