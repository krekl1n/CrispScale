package net.rosemods.betteruiscale.mixin;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.Projection;
import net.rosemods.betteruiscale.ScaleFactorUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiRenderer.class)
public class MixinGuiRenderer {
    @Unique
    private static final org.slf4j.Logger betteruiscale$LOG = org.slf4j.LoggerFactory.getLogger("crispscale");

    @Unique
    private static boolean betteruiscale$loggedDraw = false;

    @Redirect(
        method = "draw",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Projection;setupOrtho(FFFFZ)V")
    )
    private void betteruiscale$setRealGuiProjection(Projection instance, float zNear, float zFar, float width, float height, boolean invertY) {
        Window window = Minecraft.getInstance().getWindow();
        double realScale = ScaleFactorUtil.fromInternalScaleFactor(window.getGuiScale());
        if (!betteruiscale$loggedDraw) {
            betteruiscale$loggedDraw = true;
            betteruiscale$LOG.info("[cs] draw(): window guiScale={}, fb={}x{}, real={}, ortho={}x{}", window.getGuiScale(), window.getWidth(), window.getHeight(), realScale, window.getWidth() / realScale, window.getHeight() / realScale);
        }
        instance.setupOrtho(zNear, zFar, (float) (window.getWidth() / realScale), (float) (window.getHeight() / realScale), invertY);
    }

    @Unique
    private static int betteruiscale$scissorLogs = 0;

    /**
     * @author Rose
     * @author Qendolin
     * @reason Modifies gui scaling
     */
    @Overwrite
    private void enableScissor(ScreenRectangle rectangle, RenderPass renderPass) {
        Window window = Minecraft.getInstance().getWindow();
        double realScale = ScaleFactorUtil.fromInternalScaleFactor(window.getGuiScale());

        int left = (int) Math.round(rectangle.left() * realScale);
        int top = (int) Math.round(rectangle.top() * realScale);
        int right = Math.min((int) Math.round(rectangle.right() * realScale), window.getWidth());
        int bottom = Math.min((int) Math.round(rectangle.bottom() * realScale), window.getHeight());

        if (betteruiscale$scissorLogs < 5) {
            betteruiscale$scissorLogs++;
            betteruiscale$LOG.info("[cs] scissor: rect={}x{} -> px l={} t={} r={} b={}", rectangle.width(), rectangle.height(), left, top, right, bottom);
        }

        renderPass.enableScissor(
            left,
            window.getHeight() - bottom,
            Math.max(0, right - left),
            Math.max(0, bottom - top)
        );
    }
}
