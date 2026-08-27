package net.rosemods.betteruiscale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;

public final class GuiScaleHandler {
    private static boolean pending = false;

    private GuiScaleHandler() {
    }

    public static void onGuiScaleChanged() {
        Minecraft client = Minecraft.getInstance();
        if (client.gui.screen() instanceof VideoSettingsScreen) {
            pending = true;
        } else {
            client.resizeGui();
        }
    }

    public static void markPending() {
        pending = true;
    }

    public static void applyIfPending() {
        if (pending) {
            pending = false;
            Minecraft.getInstance().resizeGui();
        }
    }
}
