package com.diosmc.client.modules.hud;

import com.diosmc.client.modules.Module;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class FPSCounter extends Module {

    private boolean registered = false;

    public FPSCounter() {
        super("FPSCounter", "Muestra los FPS actuales con indicador de color", Category.HUD, GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void onEnable() {
        if (!registered) {
            HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
                if (!isEnabled()) return;
                render(matrices);
            });
            registered = true;
        }
    }

    private void render(MatrixStack matrices) {
        int fps = mc.getCurrentFps();
        int screenHeight = mc.getWindow().getScaledHeight();

        String color;
        if (fps >= 60)      color = "§a";
        else if (fps >= 30) color = "§e";
        else                color = "§c";

        String text = color + fps + " §7FPS";
        mc.textRenderer.drawWithShadow(matrices, text, 2, screenHeight - 32, 0xFFFFFF);
    }
}
