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

    public String activeTierlist = "EUROPEAN";

    public String displayType = "HIGHEST";

    public String specificMode = "CRYSTAL";

    public boolean showInTab = true;

    public boolean showNametags = true;

    public boolean showTierlistLogo = true;

    public boolean showModeIcon = true;

    public boolean showUnranked = false;

    public boolean hideOwnTag = false;

    public boolean useFallbackTierlists = false;

    public int refreshIntervalSeconds = 120;

    public int iconSize = 16;

    public String nameColor = "#FFFFFF";

    public Map<String, String> tierColors = createDefaultTierColors();

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static Map<String, String> createDefaultTierColors() {
        Map<String, String> colors = new LinkedHashMap<>();

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
                TierTaggerConfig config = GSON.fromJson(
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

        TierTaggerConfig config = new TierTaggerConfig();
        config.save();

        return config;
    }

    private void fixMissingValues() {
        if (tierColors == null) {
            tierColors = createDefaultTierColors();
        }

        Map<String, String> defaults = createDefaultTierColors();

        for (Map.Entry<String, String> entry : defaults.entrySet()) {
            tierColors.putIfAbsent(entry.getKey(), entry.getValue());
        }

        if (activeTierlist == null) {
            activeTierlist = "EUROPEAN";
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
    }

    public void save() {
        try {
            Files.createDirectories(getConfigPath().getParent());

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
