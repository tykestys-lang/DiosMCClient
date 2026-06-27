package com.diosmc.client.modules.visual;

import com.diosmc.client.modules.Module;
import org.lwjgl.glfw.GLFW;

public class Fullbright extends Module {

    private float originalGamma;

    public Fullbright() {
        super("Fullbright", "Ilumina todo el mundo sin necesidad de antorchas", Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void onEnable() {
        originalGamma = mc.options.gamma;
        mc.options.gamma = 16.0f;
    }

    @Override
    public void onDisable() {
        mc.options.gamma = originalGamma;
    }
}
