package net.rosemods.betteruiscale.mixin;

import com.mojang.blaze3d.platform.Window;
import net.rosemods.betteruiscale.RealScaleFactorAccessor;
import net.rosemods.betteruiscale.ScaleFactorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Window.class)
public class MixinWindow implements RealScaleFactorAccessor {
    @Unique
    private static final Logger betteruiscale$LOG = LoggerFactory.getLogger("crispscale");

    @Shadow
    private int framebufferWidth;

    @Shadow
    private int framebufferHeight;

    @Shadow
    private int guiScale;

    @Shadow
    private int guiScaledWidth;

    @Shadow
    private int guiScaledHeight;

    @Unique
    private double betteruiscale$realScaleFactor = 1.0;

    /**
     * @author Rose
     * @author Qendolin
     * @reason Modifies gui scaling
     */
    @Overwrite
    public int calculateScale(int guiScale, boolean forceUnicodeFont) {
        int result = ScaleFactorUtil.calcScaleFactor(guiScale, forceUnicodeFont, framebufferWidth, framebufferHeight);
        betteruiscale$LOG.info("[cs] calculateScale(requested={}, unicode={}) fb={}x{} -> internal={}", guiScale, forceUnicodeFont, framebufferWidth, framebufferHeight, result);
        return result;
    }

    /**
     * @author Rose
     * @author Qendolin
     * @reason Modifies gui scaling
     */
    @Overwrite
    public void setGuiScale(int internalScaleFactor) {
        this.guiScale = internalScaleFactor;
        this.betteruiscale$realScaleFactor = ScaleFactorUtil.fromInternalScaleFactor(internalScaleFactor);
        guiScaledWidth = ScaleFactorUtil.scaleInternal(framebufferWidth, internalScaleFactor);
        guiScaledHeight = ScaleFactorUtil.scaleInternal(framebufferHeight, internalScaleFactor);
        betteruiscale$LOG.info("[cs] setGuiScale(internal={}) -> real={}, guiScaled={}x{}", internalScaleFactor, betteruiscale$realScaleFactor, guiScaledWidth, guiScaledHeight);
    }

    @Unique
    public double betteruiscale$getRealScaleFactor() {
        return this.betteruiscale$realScaleFactor;
    }
}
