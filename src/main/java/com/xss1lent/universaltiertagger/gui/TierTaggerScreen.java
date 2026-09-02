package com.xss1lent.universaltiertagger.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class TierTaggerScreen extends Screen {

    public TierTaggerScreen() {
        super(Text.literal("Universal TierTagger"));
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
                40,
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
