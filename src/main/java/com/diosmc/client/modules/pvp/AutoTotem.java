package com.diosmc.client.modules.pvp;

import com.diosmc.client.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

/**
 * AutoTotem — mueve automáticamente un tótem de la mano
 * o del inventario a la mano offhand cuando la vida es baja.
 */
public class AutoTotem extends Module {

    private int healthThreshold = 20; // siempre activo (vida máxima)
    private boolean registered = false;

    public AutoTotem() {
        super("AutoTotem", "Equipa tótem en offhand automáticamente", Category.PVP, GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void onEnable() {
        if (!registered) {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (!isEnabled() || client.player == null || client.interactionManager == null) return;

                // Si ya hay tótem en offhand, no hacer nada
                ItemStack offhand = client.player.getOffHandStack();
                if (offhand.getItem() == Items.TOTEM_OF_UNDYING) return;

                // Buscar tótem en el inventario (slots 0-35)
                for (int i = 0; i < client.player.inventory.size(); i++) {
                    ItemStack stack = client.player.inventory.getStack(i);
                    if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                        // Mover al offhand (slot 45 en el inventario del jugador)
                        client.interactionManager.clickSlot(
                            client.player.currentScreenHandler.syncId,
                            i < 9 ? i + 36 : i, // hotbar vs inventario
                            0,
                            SlotActionType.PICKUP,
                            client.player
                        );
                        // Click en el slot offhand
                        client.interactionManager.clickSlot(
                            client.player.currentScreenHandler.syncId,
                            45,
                            0,
                            SlotActionType.PICKUP,
                            client.player
                        );
                        // Devolver lo que quedaba en cursor (si había algo)
                        client.interactionManager.clickSlot(
                            client.player.currentScreenHandler.syncId,
                            i < 9 ? i + 36 : i,
                            0,
                            SlotActionType.PICKUP,
                            client.player
                        );
                        break;
                    }
                }
            });
            registered = true;
        }
    }

    public int getHealthThreshold() { return healthThreshold; }
    public void setHealthThreshold(int v) { this.healthThreshold = v; }
}
