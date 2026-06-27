package com.diosmc.client.gui;

import com.diosmc.client.DiosMCClient;
import com.diosmc.client.modules.Module;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.List;

/**
 * ClickGUI — menú para activar/desactivar módulos con el mouse.
 * Se abre con la tecla RSHIFT (configurable).
 * Muestra tres columnas: PvP | HUD | Visual
 */
public class ClickGUI extends Screen {

    // Dimensiones de cada panel
    private static final int PANEL_WIDTH  = 100;
    private static final int PANEL_X_PVP  = 10;
    private static final int PANEL_X_HUD  = 120;
    private static final int PANEL_X_VIS  = 230;
    private static final int PANEL_Y      = 10;
    private static final int HEADER_H     = 14;
    private static final int ENTRY_H      = 12;
    private static final int PADDING      = 2;

    public ClickGUI() {
        super(new LiteralText("DiosMC Client"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Fondo oscuro semitransparente
        fillGradient(matrices, 0, 0, width, height, 0xC0101010, 0xC0101010);

        renderPanel(matrices, "§b⚔ PvP",    Module.Category.PVP,    PANEL_X_PVP, PANEL_Y, mouseX, mouseY);
        renderPanel(matrices, "§e⬛ HUD",    Module.Category.HUD,    PANEL_X_HUD, PANEL_Y, mouseX, mouseY);
        renderPanel(matrices, "§a👁 Visual", Module.Category.VISUAL, PANEL_X_VIS, PANEL_Y, mouseX, mouseY);

        // Watermark
        String wm = "§b§lDiosMC §r§7v" + DiosMCClient.VERSION + " §8— §7RSHIFT para cerrar";
        textRenderer.drawWithShadow(matrices, wm, 5, height - 12, 0xAAAAAA);

        super.render(matrices, mouseX, mouseY, delta);
    }

    private void renderPanel(MatrixStack matrices, String title, Module.Category cat,
                             int x, int y, int mouseX, int mouseY) {
        List<Module> modules = DiosMCClient.moduleManager.getByCategory(cat);
        int panelH = HEADER_H + modules.size() * (ENTRY_H + PADDING) + PADDING;

        // Fondo del panel
        fill(matrices, x, y, x + PANEL_WIDTH, y + panelH, 0xDD1A1A2E);
        // Borde superior con color
        fill(matrices, x, y, x + PANEL_WIDTH, y + 2, getCategoryColor(cat));
        // Título
        textRenderer.drawWithShadow(matrices, title, x + 4, y + 4, 0xFFFFFF);

        // Entradas de módulos
        int entryY = y + HEADER_H;
        for (Module m : modules) {
            boolean hovered = mouseX >= x && mouseX <= x + PANEL_WIDTH
                           && mouseY >= entryY && mouseY <= entryY + ENTRY_H;

            // Fondo entry
            int bg = m.isEnabled() ? 0xBB16213E : (hovered ? 0x66FFFFFF : 0x44000000);
            fill(matrices, x + 1, entryY, x + PANEL_WIDTH - 1, entryY + ENTRY_H, bg);

            // Indicador de estado
            int dot = m.isEnabled() ? 0x00FF88 : 0xFF4444;
            fill(matrices, x + 2, entryY + 3, x + 6, entryY + 9, dot);

            // Nombre del módulo
            String label = (m.isEnabled() ? "§f" : "§7") + m.getName();
            textRenderer.drawWithShadow(matrices, label, x + 9, entryY + 2, 0xFFFFFF);

            entryY += ENTRY_H + PADDING;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        toggleModuleAt((int) mouseX, (int) mouseY, Module.Category.PVP,    PANEL_X_PVP);
        toggleModuleAt((int) mouseX, (int) mouseY, Module.Category.HUD,    PANEL_X_HUD);
        toggleModuleAt((int) mouseX, (int) mouseY, Module.Category.VISUAL, PANEL_X_VIS);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void toggleModuleAt(int mx, int my, Module.Category cat, int panelX) {
        if (mx < panelX || mx > panelX + PANEL_WIDTH) return;

        List<Module> modules = DiosMCClient.moduleManager.getByCategory(cat);
        int entryY = PANEL_Y + HEADER_H;

        for (Module m : modules) {
            if (my >= entryY && my <= entryY + ENTRY_H) {
                m.toggle();
                return;
            }
            entryY += ENTRY_H + PADDING;
        }
    }

    private int getCategoryColor(Module.Category cat) {
        switch (cat) {
            case PVP:    return 0xFF0077FF;
            case HUD:    return 0xFFFFAA00;
            case VISUAL: return 0xFF00CC66;
            default:     return 0xFFFFFFFF;
        }
    }

    @Override
    public boolean shouldPause() { return false; } // no pausa el juego
}
