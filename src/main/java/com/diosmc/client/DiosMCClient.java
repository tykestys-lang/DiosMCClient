package com.diosmc.client;

import com.diosmc.client.gui.ClickGUI;
import com.diosmc.client.modules.ModuleManager;
import com.diosmc.client.events.EventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class DiosMCClient implements ClientModInitializer {

    public static final String MOD_ID = "diosmc_client";
    public static final String MOD_NAME = "DiosMC Client";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static DiosMCClient INSTANCE;
    public static ModuleManager moduleManager;
    public static EventBus eventBus;

    private static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        LOGGER.info("[DiosMC Client] Iniciando v" + VERSION + " (Fabric)");

        eventBus = new EventBus();
        moduleManager = new ModuleManager();
        moduleManager.registerModules();

        // Registrar tecla para abrir la ClickGUI (RSHIFT)
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.diosmc_client.gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "DiosMC Client"
        ));

        // Abrir GUI al presionar RSHIFT
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (guiKey.wasPressed() && client.currentScreen == null) {
                client.setScreen(new ClickGUI());
            }
        });

        LOGGER.info("[DiosMC Client] " + moduleManager.getModules().size() + " módulos cargados. GUI: RSHIFT");
    }
}
