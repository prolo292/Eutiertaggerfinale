package com.xss1lent.universaltiertagger.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import com.xss1lent.universaltiertagger.gui.TierConfigScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class KeyBindings {

    private static KeyMapping openMenuKey;

    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(
                    Identifier.fromNamespaceAndPath(
                            "universal_tiertagger",
                            "general"
                    )
            );

    public static void register() {

        openMenuKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.universal_tiertagger.open_menu",
                        InputConstants.Type.KEYSYM,
                        InputConstants.KEY_Y,
                        CATEGORY
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (openMenuKey.consumeClick()) {

                client.setScreen(
                        new TierConfigScreen(client.screen)
                );
            }
        });
    }
}
