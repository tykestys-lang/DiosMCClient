package com.diosmc.client.modules.pvp;

import com.diosmc.client.modules.Module;
import org.lwjgl.glfw.GLFW;

/**
 * Reach — aumenta el alcance de ataque/interacción.
 *
 * En Fabric, el reach real se modifica via Mixin sobre:
 *   net.minecraft.client.network.ClientPlayerInteractionManager
 *   métodos: getReachDistance() / hasExtendedReach()
 *
 * El Mixin correspondiente está en:
 *   mixin/ReachMixin.java
 */
public class Reach extends Module {

    private double reachDistance = 3.5;

    public Reach() {
        super("Reach", "Aumenta el alcance de ataque del jugador", Category.PVP, GLFW.GLFW_KEY_UNKNOWN);
    }

    // El valor se lee desde ReachMixin cuando el módulo está activo
    public double getReachDistance() { return reachDistance; }
    public void setReachDistance(double d) { this.reachDistance = Math.min(d, 6.0); }
}
