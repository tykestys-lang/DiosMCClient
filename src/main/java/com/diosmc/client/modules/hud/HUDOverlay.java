package com.diosmc.client.modules.hud;

import com.diosmc.client.DiosMCClient;
import com.diosmc.client.modules.Module;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.ArrayList;

public class HUDOverlay extends Module {

    private boolean registered = false;

    public HUDOverlay() {
        super("HUDOverlay", "Lista de módulos activos + watermark del cliente", Category.HUD, GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void onEnable() {
        if (!registered) {
            HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
                if (!isEnabled() || mc.player == null) return;
                render(matrixStack);
            });
            registered = true;
        }
    }

    private void render(MatrixStack matrices) {
        TextRenderer font = mc.textRenderer;
        int screenWidth = mc.getWindow().getScaledWidth();

        // Watermark
        String watermark = "§b§lDiosMC §r§7v" + DiosMCClient.VERSION;
        font.drawWithShadow(matrices, watermark, 2, 2, 0xFFFFFF);

        // Módulos activos (esquina superior derecha)
        List<String> active = new ArrayList<>();
        for (Module m : DiosMCClient.moduleManager.getModules()) {
            if (m.isEnabled() && !(m instanceof HUDOverlay))
                active.add(m.getName());
        }

        int y = 2;
        for (String name : active) {
            int x = screenWidth - font.getWidth(name) - 2;
            // Fondo semitransparente
            fill(matrices, x - 1, y - 1, x + font.getWidth(name) + 1, y + font.fontHeight + 1, 0x55000000);
            font.drawWithShadow(matrices, "§f" + name, x, y, 0xFFFFFF);
            y += font.fontHeight + 2;
        }
    }

    // Dibuja rectángulo sólido (helper)
    private void fill(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {
        net.minecraft.client.gui.DrawableHelper.fill(matrices, x1, y1, x2, y2, color);
    }
}
