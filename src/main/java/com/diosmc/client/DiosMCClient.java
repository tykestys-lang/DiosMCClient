package com.diosmc.client;

import com.diosmc.client.modules.ModuleManager;
import com.diosmc.client.events.EventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class DiosMCClient implements ClientModInitializer {

    public static final String MOD_ID = "diosmc_client";
    public static final String MOD_NAME = "DiosMC Client";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static DiosMCClient INSTANCE;
    public static ModuleManager moduleManager;
    public static EventBus eventBus;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        LOGGER.info("[DiosMC Client] Iniciando v" + VERSION + " (Fabric)");

        eventBus = new EventBus();
        moduleManager = new ModuleManager();
        moduleManager.registerModules();

        LOGGER.info("[DiosMC Client] " + moduleManager.getModules().size() + " módulos cargados.");
    }
}
