package com.xss1lent.universaltiertagger.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import com.xss1lent.universaltiertagger.data.GameMode;
import com.xss1lent.universaltiertagger.data.PlayerTierData;
import com.xss1lent.universaltiertagger.data.TierlistType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

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

        var client =
                net.minecraft.client.MinecraftClient.getInstance();

        if (client.player != null) {
            client.player.sendMessage(
                    Text.literal("§6[EU Tiers] §eSearching tiers for §f" + username + "§e..."),
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

        CompletableFuture.allOf(
                european,
                mctiers,
                mcpvp
        ).thenRun(() -> {

            try {

                PlayerTierData euData = european.join();
                PlayerTierData mcTiersData = mctiers.join();
                PlayerTierData mcpvpData = mcpvp.join();

                client.execute(() -> {

                    if (client.player == null) {
                        return;
                    }

                    client.player.sendMessage(
                            Text.literal(""),
                            false
                    );

                    client.player.sendMessage(
                            Text.literal("§6§l════════════════════════════"),
                            false
                    );

                    client.player.sendMessage(
                            Text.literal("§e§lTIERS FOR §f§l" + username),
                            false
                    );

                    displayTierlist(
                            "§b§lEUROPEAN TIERS",
                            euData
                    );

                    displayTierlist(
                            "§a§lMC TIERS",
                            mcTiersData
                    );

                    displayTierlist(
                            "§d§lMCPVP §f§lBETA",
                            mcpvpData
                    );

                    client.player.sendMessage(
                            Text.literal("§6§l════════════════════════════"),
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

        var client =
                net.minecraft.client.MinecraftClient.getInstance();

        if (client.player == null) {
            return;
        }

        client.player.sendMessage(
                Text.literal(""),
                false
        );

        client.player.sendMessage(
                Text.literal(title),
                false
        );

        if (data == null || !data.hasAnyTier()) {

            client.player.sendMessage(
                    Text.literal("§7No tiers found."),
                    false
            );

            return;
        }

        for (Map.Entry<GameMode, String> entry
                : data.getAllTiers().entrySet()) {

            GameMode mode = entry.getKey();
            String tier = entry.getValue();

            client.player.sendMessage(
                    Text.literal(
                            " §8• §f"
                                    + mode.getDisplayName()
                                    + ": "
                                    + getTierColor(tier)
                                    + tier
                    ),
                    false
            );
        }
    }

    private static String getTierColor(String tier) {

        if (tier == null) {
            return "§7";
        }

        return switch (tier.toUpperCase()) {

            case "HT1" -> "§6";
            case "LT1" -> "§e";

            case "HT2" -> "§a";
            case "LT2" -> "§2";

            case "HT3" -> "§b";
            case "LT3" -> "§3";

            case "HT4" -> "§9";
            case "LT4" -> "§1";

            case "HT5" -> "§d";
            case "LT5" -> "§5";

            default -> "§7";
        };
    }
}
