package com.xss1lent.universaltiertagger.keybind;

import com.xss1lent.universaltiertagger.gui.TierConfigScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    private static KeyMapping openMenuKey;

    public static void register() {

        openMenuKey = new KeyMapping(
                "key.universal_tiertagger.open_menu",
                new KeyEvent(
                        GLFW.GLFW_KEY_Y,
                        GLFW.GLFW_KEY_Y,
                        0
                ),
                "category.universal_tiertagger"
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (openMenuKey.consumeClick()) {

                Minecraft minecraft = Minecraft.getInstance();

                minecraft.setScreen(
                        new TierConfigScreen(minecraft.screen)
                );
            }
        });
    }
}
