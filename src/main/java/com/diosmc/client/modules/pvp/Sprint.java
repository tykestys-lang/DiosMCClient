package com.diosmc.client.modules.pvp;

import com.diosmc.client.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

public class Sprint extends Module {

    private boolean omniSprint = false;
    private boolean registered = false;

    public Sprint() {
        super("Sprint", "Sprint automático continuo", Category.PVP, GLFW.GLFW_KEY_V);
    }

    @Override
    public void onEnable() {
        if (!registered) {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (!isEnabled() || client.player == null) return;

                boolean moving = client.player.input.movementForward != 0
                        || (omniSprint && client.player.input.movementSideways != 0);

                if (moving && !client.player.isSprinting()) {
                    client.player.setSprinting(true);
                }
            });
            registered = true;
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) mc.player.setSprinting(false);
    }

    public boolean isOmniSprint() { return omniSprint; }
    public void setOmniSprint(boolean v) { this.omniSprint = v; }
}
