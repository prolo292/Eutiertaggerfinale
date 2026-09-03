package com.xss1lent.universaltiertagger.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class TierTaggerConfig {

    /*
     * Primary tierlist.
     * This tier appears next to the player name.
     */
    public String primaryTierlist = "EUROPEAN";

    /*
     * Secondary tierlist.
     * This tier appears above the primary nametag.
     */
    public String secondaryTierlist = "MCTIERS";

    /*
     * Enable or disable the secondary tier display.
     */
    public boolean showSecondaryTierlist = true;

    /*
     * Legacy option kept for compatibility.
     */
    public String activeTierlist = "EUROPEAN";

    /*
     * HIGHEST = Show the best tier.
     * SPECIFIC = Show a selected game mode.
     */
    public String displayType = "HIGHEST";

    /*
     * Used when displayType is SPECIFIC.
     */
    public String specificMode = "CRYSTAL";

    public boolean showInTab = true;

    public boolean showNametags = true;

    public boolean showTierlistLogo = true;

    public boolean showModeIcon = true;

    public boolean showUnranked = false;

    public boolean hideOwnTag = false;

    public boolean useFallbackTierlists = false;

    /*
     * How often player data can be refreshed.
     */
    public int refreshIntervalSeconds = 120;

    /*
     * Size used by tier/mode icons.
     */
    public int iconSize = 16;

    public String nameColor = "#FFFFFF";

    public Map<String, String> tierColors =
            createDefaultTierColors();

    private static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    private static Map<String, String> createDefaultTierColors() {

        Map<String, String> colors =
                new LinkedHashMap<>();

        colors.put("HT1", "#FFD700");
        colors.put("LT1", "#E6C15A");

        colors.put("HT2", "#C7D5E8");
        colors.put("LT2", "#9EA7B3");

        colors.put("HT3", "#E89A57");
        colors.put("LT3", "#C77A3D");

        colors.put("HT4", "#B57DA3");
        colors.put("LT4", "#747887");

        colors.put("HT5", "#9A8BB8");
        colors.put("LT5", "#666B78");

        /*
         * MC Tiers numeric ranks.
         */
        colors.put("1", "#FFD700");
        colors.put("2", "#C7D5E8");
        colors.put("3", "#E89A57");
        colors.put("4", "#B57DA3");
        colors.put("5", "#666B78");

        colors.put("UNRANKED", "#808080");

        return colors;
    }

    private static Path getConfigPath() {

        return FabricLoader.getInstance()
                .getConfigDir()
                .resolve("universal-tiertagger.json");
    }

    public static TierTaggerConfig load() {

        Path path = getConfigPath();

        try {

            if (Files.exists(path)) {

                TierTaggerConfig config =
                        GSON.fromJson(
                                Files.readString(path),
                                TierTaggerConfig.class
                        );

                if (config != null) {

                    config.fixMissingValues();
                    config.save();

                    return config;
                }
            }

        } catch (Exception exception) {

            UniversalTierTaggerClient.LOGGER.error(
                    "Failed to load TierTagger configuration",
                    exception
            );
        }

        TierTaggerConfig config =
                new TierTaggerConfig();

        config.save();

        return config;
    }

    private void fixMissingValues() {

        if (tierColors == null) {
            tierColors = createDefaultTierColors();
        }

        Map<String, String> defaults =
                createDefaultTierColors();

        for (Map.Entry<String, String> entry
                : defaults.entrySet()) {

            tierColors.putIfAbsent(
                    entry.getKey(),
                    entry.getValue()
            );
        }

        /*
         * Upgrade old configs automatically.
         */
        if (primaryTierlist == null
                || primaryTierlist.isBlank()) {

            primaryTierlist =
                    activeTierlist != null
                            ? activeTierlist
                            : "EUROPEAN";
        }

        if (secondaryTierlist == null
                || secondaryTierlist.isBlank()) {

            secondaryTierlist = "MCTIERS";
        }

        if (activeTierlist == null) {
            activeTierlist = primaryTierlist;
        }

        if (displayType == null) {
            displayType = "HIGHEST";
        }

        if (specificMode == null) {
            specificMode = "CRYSTAL";
        }

        if (refreshIntervalSeconds < 20) {
            refreshIntervalSeconds = 20;
        }

        if (iconSize < 8) {
            iconSize = 8;
        }

        if (iconSize > 32) {
            iconSize = 32;
        }
    }

    public void save() {

        try {

            Files.createDirectories(
                    getConfigPath().getParent()
            );

            Files.writeString(
                    getConfigPath(),
                    GSON.toJson(this)
            );

        } catch (IOException exception) {

            UniversalTierTaggerClient.LOGGER.error(
                    "Failed to save TierTagger configuration",
                    exception
            );
        }
    }
}
