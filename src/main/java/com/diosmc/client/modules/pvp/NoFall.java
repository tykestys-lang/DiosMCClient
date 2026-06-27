package com.diosmc.client.modules.pvp;

import com.diosmc.client.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

/**
 * NoFall — evita el daño por caída enviando un paquete OnGround=true
 * justo antes de tocar el suelo.
 */
public class NoFall extends Module {

    private boolean registered = false;

    public NoFall() {
        super("NoFall", "Cancela el daño por caída", Category.PVP, GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void onEnable() {
        if (!registered) {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (!isEnabled() || client.player == null || client.world == null) return;

                // Si estamos cayendo y vamos a golpear el suelo pronto
                if (client.player.fallDistance > 2.0f && !client.player.isOnGround()) {
                    // Enviar paquete diciendo que estamos en el suelo
                    client.player.networkHandler.sendPacket(
                        new PlayerMoveC2SPacket.OnGroundOnly(true)
                    );
                }
            });
            registered = true;
        }
    }
}
