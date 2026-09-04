package com.xss1lent.universaltiertagger.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TierConfigScreen extends Screen {

    private final Screen parent;

    public TierConfigScreen(Screen parent) {
        super(Component.literal("EU Tiers Configuration"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Primary tierlist
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Primary Tierlist: European"),
                        button -> {
                            // Will be implemented next
                        }
                ).bounds(centerX - 100, centerY - 70, 200, 20).build()
        );

        // Secondary tierlist
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Secondary Tierlist: MCTiers"),
                        button -> {
                            // Will be implemented next
                        }
                ).bounds(centerX - 100, centerY - 40, 200, 20).build()
        );

        // Secondary toggle
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Show Secondary: ON"),
                        button -> {
                            // Will be implemented next
                        }
                ).bounds(centerX - 100, centerY - 10, 200, 20).build()
        );

        // Display mode
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Display: Highest Tier"),
                        button -> {
                            // Will be implemented next
                        }
                ).bounds(centerX - 100, centerY + 20, 200, 20).build()
        );

        // Done button
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Done"),
                        button -> {
                            this.onClose();
                        }
                ).bounds(centerX - 100, centerY + 60, 200, 20).build()
        );
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
                this.height / 2 - 110,
                0xFFFFFF
        );

        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }
}
