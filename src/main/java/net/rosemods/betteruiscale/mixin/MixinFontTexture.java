package net.rosemods.betteruiscale.mixin;

import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.systems.SamplerCache;
import net.minecraft.client.gui.font.FontTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FontTexture.class)
public class MixinFontTexture {
    @Redirect(
        method = "<init>",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/SamplerCache;getRepeat(Lcom/mojang/blaze3d/textures/FilterMode;)Lcom/mojang/blaze3d/textures/GpuSampler;")
    )
    private GpuSampler betteruiscale$setBilinearFiltering(SamplerCache instance, FilterMode filterMode) {
        return instance.getRepeat(FilterMode.LINEAR);
    }
}
