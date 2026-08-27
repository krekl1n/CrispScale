package net.rosemods.betteruiscale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends OptionsSubScreen {
    protected final Config config;

    public ConfigScreen(Screen parent, Config config) {
        super(parent, Minecraft.getInstance().options, Component.translatable("crispscale.options.title"));
        this.config = config;
    }

    @Override
    protected void addOptions() {
        this.list.addBig(config.fontSmoothing());
    }

    @Override
    public void removed() {
        super.removed();
        config.save(Main.configPath());
    }
}
