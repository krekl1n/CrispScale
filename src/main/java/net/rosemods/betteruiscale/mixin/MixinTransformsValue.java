package net.rosemods.betteruiscale.mixin;

import com.mojang.blaze3d.buffers.Std140Builder;
import net.minecraft.client.renderer.DynamicUniforms;
import net.rosemods.betteruiscale.Config;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DynamicUniforms.Transform.class)
public class MixinTransformsValue {
    @Redirect(
        method = "write",
        at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/buffers/Std140Builder;putVec3(Lorg/joml/Vector3fc;)Lcom/mojang/blaze3d/buffers/Std140Builder;")
    )
    private Std140Builder betteruiscale$putSmoothness(Std140Builder builder, Vector3fc modelOffset) {
        return builder.putVec3(modelOffset).putFloat(Config.CURRENT_FONT_SMOOTHING);
    }
}
