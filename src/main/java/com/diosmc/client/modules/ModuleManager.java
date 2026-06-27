package com.diosmc.client.modules;

import com.diosmc.client.modules.pvp.KillAura;
import com.diosmc.client.modules.pvp.Sprint;
import com.diosmc.client.modules.pvp.Reach;
import com.diosmc.client.modules.hud.HUDOverlay;
import com.diosmc.client.modules.hud.Coordinates;
import com.diosmc.client.modules.hud.FPSCounter;
import com.diosmc.client.modules.visual.ESP;
import com.diosmc.client.modules.visual.Fullbright;
import com.diosmc.client.modules.visual.Zoom;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public void registerModules() {
        // PvP
        register(new KillAura());
        register(new Sprint());
        register(new Reach());
        // HUD
        register(new HUDOverlay());
        register(new Coordinates());
        register(new FPSCounter());
        // Visual
        register(new ESP());
        register(new Fullbright());
        register(new Zoom());
    }

    public void register(Module module) {
        modules.add(module);
    }

    public Module getModuleByName(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public List<Module> getModules() { return modules; }

    public List<Module> getByCategory(Module.Category cat) {
        List<Module> result = new ArrayList<>();
        for (Module m : modules)
            if (m.getCategory() == cat) result.add(m);
        return result;
    }

    public void onUpdate() {
        for (Module m : modules)
            if (m.isEnabled()) m.onUpdate();
    }
}
