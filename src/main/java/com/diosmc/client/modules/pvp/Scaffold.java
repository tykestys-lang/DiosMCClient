package com.diosmc.client.modules.pvp;

import com.diosmc.client.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/**
 * Scaffold — coloca automáticamente bloques bajo los pies del jugador
 * mientras se camina en el aire.
 */
public class Scaffold extends Module {

    private boolean registered = false;
    private boolean rotate = true; // rotar hacia abajo al colocar

    public Scaffold() {
        super("Scaffold", "Coloca bloques bajo tus pies automáticamente", Category.PVP, GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void onEnable() {
        if (!registered) {
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (!isEnabled() || client.player == null || client.world == null) return;
                if (client.player.isOnGround()) return;

                // Posición bajo el jugador
                BlockPos below = client.player.getBlockPos().down();
                BlockState state = client.world.getBlockState(below);

                // Si ya hay bloque, no hacer nada
                if (!state.isAir()) return;

                // Buscar bloque en hotbar
                int blockSlot = findBlockInHotbar(client);
                if (blockSlot == -1) return;

                int prevSlot = client.player.inventory.selectedSlot;
                client.player.inventory.selectedSlot = blockSlot;

                // Rotar hacia abajo
                if (rotate) {
                    client.player.setPitch(80.0f);
                }

                // Colocar bloque
                BlockHitResult hit = new BlockHitResult(
                    Vec3d.ofCenter(below),
                    Direction.UP,
                    below,
                    false
                );

                client.interactionManager.interactBlock(client.player, client.world, Hand.MAIN_HAND, hit);
                client.player.swingHand(Hand.MAIN_HAND);
                client.player.inventory.selectedSlot = prevSlot;
            });
            registered = true;
        }
    }

    private int findBlockInHotbar(net.minecraft.client.MinecraftClient client) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.inventory.getStack(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                // Evitar bloques no sólidos
                BlockItem bi = (BlockItem) stack.getItem();
                if (bi.getBlock() != Blocks.AIR) return i;
            }
        }
        return -1;
    }

    public boolean isRotate() { return rotate; }
    public void setRotate(boolean v) { this.rotate = v; }
}
