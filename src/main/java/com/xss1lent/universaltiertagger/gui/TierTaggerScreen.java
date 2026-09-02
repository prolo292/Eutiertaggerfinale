package com.xss1lent.universaltiertagger.gui;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class TierTaggerScreen extends Screen {

    public TierTaggerScreen() {
        super(Text.literal("Universal TierTagger"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal(
                                "Tierlist: " +
                                UniversalTierTaggerClient.CONFIG.activeTierlist
                        ),
                        button -> cycleTierlist()
                ).dimensions(centerX - 100, 70, 200, 20).build()
        );

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal(
                                "Display: " +
                                UniversalTierTaggerClient.CONFIG.displayType
                        ),
                        button -> cycleDisplayType()
                ).dimensions(centerX - 100, 100, 200, 20).build()
        );

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal(
                                "Mode: " +
                                UniversalTierTaggerClient.CONFIG.specificMode
                        ),
                        button -> cycleGameMode()
                ).dimensions(centerX - 100, 130, 200, 20).build()
        );

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal(
                                "Tab List: " +
                                (UniversalTierTaggerClient.CONFIG.showInTab
                                        ? "ON"
                                        : "OFF")
                        ),
                        button -> {
                            UniversalTierTaggerClient.CONFIG.showInTab =
                                    !UniversalTierTaggerClient.CONFIG.showInTab;

                            clearAndInit();
                        }
                ).dimensions(centerX - 100, 160, 200, 20).build()
        );

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal(
                                "Nametags: " +
                                (UniversalTierTaggerClient.CONFIG.showNametags
                                        ? "ON"
                                        : "OFF")
                        ),
                        button -> {
                            UniversalTierTaggerClient.CONFIG.showNametags =
                                    !UniversalTierTaggerClient.CONFIG.showNametags;

                            clearAndInit();
                        }
                ).dimensions(centerX - 100, 190, 200, 20).build()
        );

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Save Settings"),
                        button -> {
                            UniversalTierTaggerClient.CONFIG.save();
                            close();
                        }
                ).dimensions(centerX - 100, 230, 200, 20).build()
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

        clearAndInit();
    }

    private void cycleDisplayType() {
        if ("HIGHEST".equals(
                UniversalTierTaggerClient.CONFIG.displayType
        )) {
            UniversalTierTaggerClient.CONFIG.displayType = "SPECIFIC";
        } else {
            UniversalTierTaggerClient.CONFIG.displayType = "HIGHEST";
        }

        clearAndInit();
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

        clearAndInit();
    }

    @Override
    public void close() {
        UniversalTierTaggerClient.CONFIG.save();
        super.close();
    }

    @Override
    public void render(
            net.minecraft.client.gui.DrawContext context,
            int mouseX,
            int mouseY,
            float delta
    ) {
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(
                textRenderer,
                "Universal TierTagger",
                width / 2,
                35,
                0xFFFFFF
        );

        context.drawCenteredTextWithShadow(
                textRenderer,
                "Preview: PlayerName  LT5  Crystal",
                width / 2,
                280,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                textRenderer,
                "Made by XsS1lent",
                width - 110,
                height - 20,
                0xAAAAAA
        );
    }
}
