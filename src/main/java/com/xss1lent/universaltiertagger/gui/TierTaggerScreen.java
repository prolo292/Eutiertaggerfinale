package com.xss1lent.universaltiertagger.gui;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TierTaggerScreen extends Screen {

    public TierTaggerScreen() {
        super(Component.literal("Universal TierTagger"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        addRenderableWidget(
                Button.builder(
                        Component.literal(
                                "Tierlist: " +
                                UniversalTierTaggerClient.CONFIG.activeTierlist
                        ),
                        button -> cycleTierlist()
                ).bounds(centerX - 100, 70, 200, 20).build()
        );

        addRenderableWidget(
                Button.builder(
                        Component.literal(
                                "Display: " +
                                UniversalTierTaggerClient.CONFIG.displayType
                        ),
                        button -> cycleDisplayType()
                ).bounds(centerX - 100, 100, 200, 20).build()
        );

        addRenderableWidget(
                Button.builder(
                        Component.literal(
                                "Mode: " +
                                UniversalTierTaggerClient.CONFIG.specificMode
                        ),
                        button -> cycleGameMode()
                ).bounds(centerX - 100, 130, 200, 20).build()
        );

        addRenderableWidget(
                Button.builder(
                        Component.literal(
                                "Tab List: " +
                                (UniversalTierTaggerClient.CONFIG.showInTab
                                        ? "ON"
                                        : "OFF")
                        ),
                        button -> {
                            UniversalTierTaggerClient.CONFIG.showInTab =
                                    !UniversalTierTaggerClient.CONFIG.showInTab;

                            rebuildWidgets();
                        }
                ).bounds(centerX - 100, 160, 200, 20).build()
        );

        addRenderableWidget(
                Button.builder(
                        Component.literal(
                                "Nametags: " +
                                (UniversalTierTaggerClient.CONFIG.showNametags
                                        ? "ON"
                                        : "OFF")
                        ),
                        button -> {
                            UniversalTierTaggerClient.CONFIG.showNametags =
                                    !UniversalTierTaggerClient.CONFIG.showNametags;

                            rebuildWidgets();
                        }
                ).bounds(centerX - 100, 190, 200, 20).build()
        );

        addRenderableWidget(
                Button.builder(
                        Component.literal("Save Settings"),
                        button -> {
                            UniversalTierTaggerClient.CONFIG.save();
                            onClose();
                        }
                ).bounds(centerX - 100, 230, 200, 20).build()
        );
    }

    private void cycleTierlist() {
        String[] tierlists = {
                "EUROPEAN",
                "MCTIERS",
                "PVPTIERS",
                "MCPVP"
        };

        int currentIndex = 0;

        for (int i = 0; i < tierlists.length; i++) {
            if (tierlists[i].equals(
                    UniversalTierTaggerClient.CONFIG.activeTierlist
            )) {
                currentIndex = i;
                break;
            }
        }

        currentIndex = (currentIndex + 1) % tierlists.length;

        UniversalTierTaggerClient.CONFIG.activeTierlist =
                tierlists[currentIndex];

        rebuildWidgets();
    }

    private void cycleDisplayType() {
        UniversalTierTaggerClient.CONFIG.displayType =
                UniversalTierTaggerClient.CONFIG.displayType.equals("HIGHEST")
                        ? "SPECIFIC"
                        : "HIGHEST";

        rebuildWidgets();
    }

    private void cycleGameMode() {
        String[] modes = {
                "CRYSTAL",
                "SWORD",
                "AXE",
                "POT",
                "VANILLA",
                "SMP",
                "NETHOP",
                "UHC",
                "MACE"
        };

        int currentIndex = 0;

        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equals(
                    UniversalTierTaggerClient.CONFIG.specificMode
            )) {
                currentIndex = i;
                break;
            }
        }

        currentIndex = (currentIndex + 1) % modes.length;

        UniversalTierTaggerClient.CONFIG.specificMode =
                modes[currentIndex];

        rebuildWidgets();
    }

    @Override
    public void onClose() {
        UniversalTierTaggerClient.CONFIG.save();
        super.onClose();
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {
        super.render(graphics, mouseX, mouseY, delta);

        graphics.drawCenteredString(
                font,
                "Universal TierTagger",
                width / 2,
                35,
                0xFFFFFF
        );

        graphics.drawCenteredString(
                font,
                "Preview: PlayerName  LT5  Crystal",
                width / 2,
                280,
                0xFFFFFF
        );

        graphics.drawString(
                font,
                "Made by XsS1lent",
                width - 110,
                height - 20,
                0xAAAAAA
        );
    }
}
