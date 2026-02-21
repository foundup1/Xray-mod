package com.xraymod.client;

import com.xraymod.client.gui.XRayScreen;
import com.xraymod.client.xray.XRayManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class XRayModClient implements ClientModInitializer {

    public static KeyBinding openMenuKey;
    public static XRayManager xRayManager = new XRayManager();

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.xraymod.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                "category.xraymod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new XRayScreen());
                }
            }
        });
    }
}
