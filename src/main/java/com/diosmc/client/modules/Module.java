package com.diosmc.client.modules;

import net.minecraft.client.MinecraftClient;

public abstract class Module {

    protected final MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;
    private int keybind; // GLFW key code

    public enum Category {
        PVP, HUD, VISUAL
    }

    public Module(String name, String description, Category category, int keybind) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keybind = keybind;
        this.enabled = false;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public void onEnable()  {}
    public void onDisable() {}
    public void onUpdate()  {}
    public void onRender()  {}

    public String getName()        { return name; }
    public String getDescription() { return description; }
    public Category getCategory()  { return category; }
    public boolean isEnabled()     { return enabled; }
    public void setEnabled(boolean v) { this.enabled = v; }
    public int getKeybind()        { return keybind; }
    public void setKeybind(int k)  { this.keybind = k; }
}
