package com.xss1lent.universaltiertagger.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import com.xss1lent.universaltiertagger.gui.TierTaggerScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static KeyMapping OPEN_MENU;

    public static void register() {

        KeyMapping.Category category =
                KeyMapping.Category.register(
                        ResourceLocation.fromNamespaceAndPath(
                                "universal_tiertagger",
                                "general"
                        )
                );

        OPEN_MENU = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.universal_tiertagger.open_menu",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_Y,
                        category
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_MENU.consumeClick()) {
                client.setScreen(new TierTaggerScreen());
            }
        });
    }
}
