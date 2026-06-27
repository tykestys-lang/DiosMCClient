package com.diosmc.client.modules.hud;

import com.diosmc.client.modules.Module;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class Coordinates extends Module {

    private boolean registered = false;

    public Coordinates() {
        super("Coordinates", "Muestra XYZ, dirección y dimensión en pantalla", Category.HUD, GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void onEnable() {
        if (!registered) {
            HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
                if (!isEnabled() || mc.player == null) return;
                render(matrices);
            });
            registered = true;
        }
    }

    private void render(MatrixStack matrices) {
        int screenHeight = mc.getWindow().getScaledHeight();
        BlockPos pos = mc.player.getBlockPos();

        String coords = String.format("§7XYZ: §f%d §7/ §f%d §7/ §f%d", pos.getX(), pos.getY(), pos.getZ());
        String info   = String.format("§7Dir: §f%s  §7Dim: §f%s", getFacing(), getDimension());

        int yBase = screenHeight - 22;
        mc.textRenderer.drawWithShadow(matrices, coords, 2, yBase - mc.textRenderer.fontHeight - 1, 0xFFFFFF);
        mc.textRenderer.drawWithShadow(matrices, info,   2, yBase, 0xFFFFFF);
    }

    private String getFacing() {
        float yaw = mc.player.getYaw() % 360;
        if (yaw < 0) yaw += 360;
        if (yaw >= 315 || yaw < 45)  return "Sur (+Z)";
        if (yaw < 135)               return "Oeste (-X)";
        if (yaw < 225)               return "Norte (-Z)";
        return "Este (+X)";
    }

    private String getDimension() {
        RegistryKey<World> key = mc.world.getRegistryKey();
        if (key == World.OVERWORLD)  return "Overworld";
        if (key == World.NETHER)     return "Nether";
        if (key == World.END)        return "End";
        return key.getValue().getPath();
    }
}
