package net.rosemods.betteruiscale.mixin;

import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.rosemods.betteruiscale.GuiScaleHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoSettingsScreen.class)
public abstract class MixinVideoOptionsScreen extends OptionsSubScreen {
    @Unique
    private int betteruiscale$prevGuiScale = 0;

    public MixinVideoOptionsScreen(Screen parent, Options options) {
        super(parent, options, Component.translatable("options.videoTitle"));
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void betteruiscale$captureGuiScale(MouseButtonEvent event, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        betteruiscale$prevGuiScale = this.options.guiScale().get();
    }

    // FIXME: gui scale does not update when using keyboard controls, see: https://bugs.mojang.com/browse/MC-166361
    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        boolean result = super.mouseReleased(event);
        if (event.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT && this.options.guiScale().get() != betteruiscale$prevGuiScale) {
            GuiScaleHandler.applyIfPending();
        }
        return result;
    }

    @Inject(method = "removed", at = @At("TAIL"))
    private void betteruiscale$applyOnRemoved(CallbackInfo ci) {
        GuiScaleHandler.applyIfPending();
    }
}
