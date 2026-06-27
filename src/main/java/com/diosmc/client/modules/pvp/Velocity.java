package com.diosmc.client.modules.pvp;

import com.diosmc.client.modules.Module;
import org.lwjgl.glfw.GLFW;

/**
 * Velocity — reduce el knockback recibido.
 * Se aplica via VelocityMixin que intercepta el paquete EntityVelocityS2CPacket.
 */
public class Velocity extends Module {

    // Porcentaje de knockback que se aplica (0.0 = ninguno, 1.0 = normal)
    private float horizontalMultiplier = 0.0f;
    private float verticalMultiplier   = 0.0f;

    public Velocity() {
        super("Velocity", "Reduce o cancela el knockback recibido", Category.PVP, GLFW.GLFW_KEY_UNKNOWN);
    }

    public float getHorizontalMultiplier() { return horizontalMultiplier; }
    public void  setHorizontalMultiplier(float v) { this.horizontalMultiplier = Math.max(0f, Math.min(1f, v)); }

    public float getVerticalMultiplier() { return verticalMultiplier; }
    public void  setVerticalMultiplier(float v) { this.verticalMultiplier = Math.max(0f, Math.min(1f, v)); }
}
