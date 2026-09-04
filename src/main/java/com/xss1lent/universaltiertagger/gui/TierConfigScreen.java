package com.xss1lent.universaltiertagger.gui;

import com.xss1lent.universaltiertagger.UniversalTierTaggerClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TierConfigScreen extends Screen {

    private final Screen parent;

    private Button primaryButton;
    private Button secondaryButton;
    private Button secondaryToggleButton;
    private Button displayTypeButton;
    private Button modeButton;

    private static final String[] TIERLISTS = {
            "EUROPEAN",
            "MCTIERS",
            "MCPVP"
    };

    private static final String[] MODES = {
            "SWORD",
            "AXE",
            "UHC",
            "NETHOP",
            "POT",
            "VANILLA",
            "SMP",
            "MACE",
            "CRYSTAL"
    };

    public TierConfigScreen(Screen parent) {
        super(Component.literal("Universal TierTagger"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        primaryButton = this.addRenderableWidget(
                Button.builder(
                        Component.literal(""),
                        button -> {
                            cyclePrimaryTierlist();
                            updateButtons();
                        }
                ).bounds(centerX - 110, centerY - 85, 220, 20).build()
        );

        secondaryButton = this.addRenderableWidget(
                Button.builder(
                        Component.literal(""),
                        button -> {
                            cycleSecondaryTierlist();
                            updateButtons();
                        }
                ).bounds(centerX - 110, centerY - 55, 220, 20).build()
        );

        secondaryToggleButton = this.addRenderableWidget(
                Button.builder(
                        Component.literal(""),
                        button -> {
                            UniversalTierTaggerClient.CONFIG.showSecondaryTierlist =
                                    !UniversalTierTaggerClient.CONFIG.showSecondaryTierlist;

                            updateButtons();
                        }
                ).bounds(centerX - 110, centerY - 25, 220, 20).build()
        );

        displayTypeButton = this.addRenderableWidget(
                Button.builder(
                        Component.literal(""),
                        button -> {

                            if ("HIGHEST".equalsIgnoreCase(
                                    UniversalTierTaggerClient.CONFIG.displayType
                            )) {

                                UniversalTierTaggerClient.CONFIG.displayType =
                                        "SPECIFIC";

                            } else {

                                UniversalTierTaggerClient.CONFIG.displayType =
                                        "HIGHEST";
                            }

                            updateButtons();
                        }
                ).bounds(centerX - 110, centerY + 5, 220, 20).build()
        );

        modeButton = this.addRenderableWidget(
                Button.builder(
                        Component.literal(""),
                        button -> {
                            cycleSpecificMode();
                            updateButtons();
                        }
                ).bounds(centerX - 110, centerY + 35, 220, 20).build()
        );

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Done"),
                        button -> {
                            saveAndClose();
                        }
                ).bounds(centerX - 110, centerY + 75, 220, 20).build()
        );

        updateButtons();
    }

    private void cyclePrimaryTierlist() {

        UniversalTierTaggerClient.CONFIG.primaryTierlist =
                getNext(
                        TIERLISTS,
                        UniversalTierTaggerClient.CONFIG.primaryTierlist
                );

        UniversalTierTaggerClient.CONFIG.activeTierlist =
                UniversalTierTaggerClient.CONFIG.primaryTierlist;
    }

    private void cycleSecondaryTierlist() {

        UniversalTierTaggerClient.CONFIG.secondaryTierlist =
                getNext(
                        TIERLISTS,
                        UniversalTierTaggerClient.CONFIG.secondaryTierlist
                );
    }

    private void cycleSpecificMode() {

        UniversalTierTaggerClient.CONFIG.specificMode =
                getNext(
                        MODES,
                        UniversalTierTaggerClient.CONFIG.specificMode
                );
    }

    private String getNext(
            String[] values,
            String current
    ) {

        for (int i = 0; i < values.length; i++) {

            if (values[i].equalsIgnoreCase(current)) {
                return values[(i + 1) % values.length];
            }
        }

        return values[0];
    }

    private void updateButtons() {

        primaryButton.setMessage(
                Component.literal(
                        "Primary Tierlist: "
                                + UniversalTierTaggerClient.CONFIG.primaryTierlist
                )
        );

        secondaryButton.setMessage(
                Component.literal(
                        "Secondary Tierlist: "
                                + UniversalTierTaggerClient.CONFIG.secondaryTierlist
                )
        );

        secondaryToggleButton.setMessage(
                Component.literal(
                        "Show Secondary: "
                                + (UniversalTierTaggerClient.CONFIG.showSecondaryTierlist
                                ? "ON"
                                : "OFF")
                )
        );

        displayTypeButton.setMessage(
                Component.literal(
                        "Display: "
                                + UniversalTierTaggerClient.CONFIG.displayType
                )
        );

        modeButton.setMessage(
                Component.literal(
                        "Specific Mode: "
                                + UniversalTierTaggerClient.CONFIG.specificMode
                )
        );

        modeButton.active =
                "SPECIFIC".equalsIgnoreCase(
                        UniversalTierTaggerClient.CONFIG.displayType
                );
    }

    private void saveAndClose() {

        if (UniversalTierTaggerClient.CONFIG != null) {
            UniversalTierTaggerClient.CONFIG.save();
        }

        onClose();
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {

        this.renderBackground(
                graphics,
                mouseX,
                mouseY,
                delta
        );

        graphics.drawCenteredString(
                this.font,
                "Universal TierTagger",
                this.width / 2,
                this.height / 2 - 125,
                0xFFFFFF
        );

        graphics.drawCenteredString(
                this.font,
                "Click buttons to change settings",
                this.width / 2,
                this.height / 2 - 108,
                0xAAAAAA
        );

        super.render(
                graphics,
                mouseX,
                mouseY,
                delta
        );
    }

    @Override
    public void onClose() {

        if (UniversalTierTaggerClient.CONFIG != null) {
            UniversalTierTaggerClient.CONFIG.save();
        }

        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }
}
