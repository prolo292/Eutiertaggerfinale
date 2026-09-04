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

    private Button tabButton;
    private Button nametagButton;
    private Button modeIconButton;
    private Button logoButton;
    private Button unrankedButton;
    private Button hideOwnButton;

    private static final String[] TIERLISTS = {
            "EUROPEAN",
            "MCTIERS",
            "MCPVP"
    };

    private static final String[] EUROPEAN_MODES = {
            "SWORD",
            "AXE",
            "POT",
            "VANILLA",
            "SMP",
            "NETHOP",
            "UHC",
            "MACE",
            "CRYSTAL"
    };

    private static final String[] MCTIERS_MODES = {
            "SWORD",
            "MACE",
            "UHC",
            "AXE",
            "NETHOP",
            "POT",
            "VANILLA",
            "SMP"
    };

    private static final String[] MCPVP_MODES = {
            "SWORD",
            "SHIELD",
            "POT",
            "EARLY_GAME",
            "END_GAME",
            "MACE",
            "LATE_GAME",
            "SPEAR"
    };

    public TierConfigScreen(Screen parent) {
        super(Component.literal("Universal TierTagger"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int startY = this.height / 2 - 135;

        primaryButton = addButton(centerX, startY, button -> {
            cyclePrimaryTierlist();
            ensureValidSpecificMode();
            updateButtons();
        });

        secondaryButton = addButton(centerX, startY + 23, button -> {
            cycleSecondaryTierlist();
            updateButtons();
        });

        secondaryToggleButton = addButton(centerX, startY + 46, button -> {
            UniversalTierTaggerClient.CONFIG.showSecondaryTierlist =
                    !UniversalTierTaggerClient.CONFIG.showSecondaryTierlist;
            updateButtons();
        });

        displayTypeButton = addButton(centerX, startY + 69, button -> {

            if ("HIGHEST".equalsIgnoreCase(
                    UniversalTierTaggerClient.CONFIG.displayType
            )) {
                UniversalTierTaggerClient.CONFIG.displayType = "SPECIFIC";
            } else {
                UniversalTierTaggerClient.CONFIG.displayType = "HIGHEST";
            }

            updateButtons();
        });

        modeButton = addButton(centerX, startY + 92, button -> {
            cycleSpecificMode();
            updateButtons();
        });

        tabButton = addButton(centerX, startY + 115, button -> {
            UniversalTierTaggerClient.CONFIG.showInTab =
                    !UniversalTierTaggerClient.CONFIG.showInTab;
            updateButtons();
        });

        nametagButton = addButton(centerX, startY + 138, button -> {
            UniversalTierTaggerClient.CONFIG.showNametags =
                    !UniversalTierTaggerClient.CONFIG.showNametags;
            updateButtons();
        });

        modeIconButton = addButton(centerX, startY + 161, button -> {
            UniversalTierTaggerClient.CONFIG.showModeIcon =
                    !UniversalTierTaggerClient.CONFIG.showModeIcon;
            updateButtons();
        });

        logoButton = addButton(centerX, startY + 184, button -> {
            UniversalTierTaggerClient.CONFIG.showTierlistLogo =
                    !UniversalTierTaggerClient.CONFIG.showTierlistLogo;
            updateButtons();
        });

        unrankedButton = addButton(centerX, startY + 207, button -> {
            UniversalTierTaggerClient.CONFIG.showUnranked =
                    !UniversalTierTaggerClient.CONFIG.showUnranked;
            updateButtons();
        });

        hideOwnButton = addButton(centerX, startY + 230, button -> {
            UniversalTierTaggerClient.CONFIG.hideOwnTag =
                    !UniversalTierTaggerClient.CONFIG.hideOwnTag;
            updateButtons();
        });

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Done"),
                        button -> saveAndClose()
                ).bounds(
                        centerX - 110,
                        startY + 258,
                        220,
                        20
                ).build()
        );

        ensureValidSpecificMode();
        updateButtons();
    }

    private Button addButton(
            int centerX,
            int y,
            Button.OnPress action
    ) {
        return this.addRenderableWidget(
                Button.builder(
                        Component.literal(""),
                        action
                ).bounds(centerX - 110, y, 220, 20).build()
        );
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

        String[] modes = getModesForCurrentTierlist();

        UniversalTierTaggerClient.CONFIG.specificMode =
                getNext(
                        modes,
                        UniversalTierTaggerClient.CONFIG.specificMode
                );
    }

    private String[] getModesForCurrentTierlist() {

        String tierlist =
                UniversalTierTaggerClient.CONFIG.primaryTierlist;

        if ("MCPVP".equalsIgnoreCase(tierlist)) {
            return MCPVP_MODES;
        }

        if ("MCTIERS".equalsIgnoreCase(tierlist)) {
            return MCTIERS_MODES;
        }

        return EUROPEAN_MODES;
    }

    private void ensureValidSpecificMode() {

        String[] modes = getModesForCurrentTierlist();

        String current =
                UniversalTierTaggerClient.CONFIG.specificMode;

        for (String mode : modes) {
            if (mode.equalsIgnoreCase(current)) {
                return;
            }
        }

        UniversalTierTaggerClient.CONFIG.specificMode = modes[0];
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

    private String onOff(boolean value) {
        return value ? "ON" : "OFF";
    }

    private String formatMode(String mode) {

        return switch (mode) {
            case "EARLY_GAME" -> "Early Game";
            case "END_GAME" -> "End Game";
            case "LATE_GAME" -> "Late Game";
            case "NETHOP" -> "NethOP";
            default -> mode.substring(0, 1)
                    + mode.substring(1).toLowerCase();
        };
    }

    private void updateButtons() {

        primaryButton.setMessage(Component.literal(
                "Primary Tierlist: "
                        + UniversalTierTaggerClient.CONFIG.primaryTierlist
        ));

        secondaryButton.setMessage(Component.literal(
                "Secondary Tierlist: "
                        + UniversalTierTaggerClient.CONFIG.secondaryTierlist
        ));

        secondaryToggleButton.setMessage(Component.literal(
                "Show Secondary: "
                        + onOff(
                                UniversalTierTaggerClient.CONFIG
                                        .showSecondaryTierlist
                        )
        ));

        displayTypeButton.setMessage(Component.literal(
                "Display: "
                        + UniversalTierTaggerClient.CONFIG.displayType
        ));

        modeButton.setMessage(Component.literal(
                "Specific Mode: "
                        + formatMode(
                                UniversalTierTaggerClient.CONFIG.specificMode
                        )
        ));

        modeButton.active =
                "SPECIFIC".equalsIgnoreCase(
                        UniversalTierTaggerClient.CONFIG.displayType
                );

        tabButton.setMessage(Component.literal(
                "Show in Tab: "
                        + onOff(UniversalTierTaggerClient.CONFIG.showInTab)
        ));

        nametagButton.setMessage(Component.literal(
                "Show Nametags: "
                        + onOff(UniversalTierTaggerClient.CONFIG.showNametags)
        ));

        modeIconButton.setMessage(Component.literal(
                "Show Mode Icon: "
                        + onOff(UniversalTierTaggerClient.CONFIG.showModeIcon)
        ));

        logoButton.setMessage(Component.literal(
                "Show Tierlist Logo: "
                        + onOff(
                                UniversalTierTaggerClient.CONFIG.showTierlistLogo
                        )
        ));

        unrankedButton.setMessage(Component.literal(
                "Show Unranked: "
                        + onOff(UniversalTierTaggerClient.CONFIG.showUnranked)
        ));

        hideOwnButton.setMessage(Component.literal(
                "Hide Own Tag: "
                        + onOff(UniversalTierTaggerClient.CONFIG.hideOwnTag)
        ));
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

        this.renderBackground(graphics, mouseX, mouseY, delta);

        graphics.drawCenteredString(
                this.font,
                "Universal TierTagger",
                this.width / 2,
                15,
                0xFFFFFF
        );

        graphics.drawCenteredString(
                this.font,
                "Press buttons to configure your tier display",
                this.width / 2,
                30,
                0xAAAAAA
        );

        super.render(graphics, mouseX, mouseY, delta);
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
