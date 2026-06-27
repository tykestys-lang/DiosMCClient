package com.diosmc.client.modules.visual;

import com.diosmc.client.modules.Module;
import org.lwjgl.glfw.GLFW;

/**
 * Zoom — reduce el FOV mientras el módulo está activo.
 * El FOV real se intercepta en ZoomMixin sobre GameRenderer#getFov()
 */
public class Zoom extends Module {

    private float zoomFOV    = 10.0f;
    private float smoothness = 0.15f;
    public  float currentFOV = -1f;   // leído por ZoomMixin

    public Zoom() {
        super("Zoom", "Zoom óptico suave (tecla C)", Category.VISUAL, GLFW.GLFW_KEY_C);
    }

    @Override
    public void onDisable() {
        currentFOV = -1f;
    }

    public float getZoomFOV()    { return zoomFOV; }
    public void setZoomFOV(float v) { this.zoomFOV = Math.max(1f, v); }
    public float getSmoothness() { return smoothness; }
}
