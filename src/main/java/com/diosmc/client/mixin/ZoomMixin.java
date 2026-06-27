package com.diosmc.client.mixin;

import com.diosmc.client.DiosMCClient;
import com.diosmc.client.modules.visual.Zoom;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyReturnValue;

@Mixin(GameRenderer.class)
public class ZoomMixin {

    @ModifyReturnValue(method = "getFov", at = @At("RETURN"))
    private double onGetFov(double original) {
        if (DiosMCClient.moduleManager == null) return original;

        Zoom zoom = (Zoom) DiosMCClient.moduleManager.getModuleByName("Zoom");
        if (zoom == null || !zoom.isEnabled()) return original;

        // Interpolación suave
        if (zoom.currentFOV < 0) zoom.currentFOV = (float) original;
        zoom.currentFOV += (zoom.getZoomFOV() - zoom.currentFOV) * zoom.getSmoothness();
        return zoom.currentFOV;
    }
}
