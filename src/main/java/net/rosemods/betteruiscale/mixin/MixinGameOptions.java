package net.rosemods.betteruiscale.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.rosemods.betteruiscale.GuiScaleHandler;
import net.rosemods.betteruiscale.MaxSuppliableIntSliderCallbacks;
import net.rosemods.betteruiscale.ScaleFactorUtil;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Options.class)
public class MixinGameOptions {
    @ModifyArgs(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/OptionInstance;<init>(Ljava/lang/String;Lnet/minecraft/client/OptionInstance$TooltipSupplier;Lnet/minecraft/client/OptionInstance$CaptionBasedToString;Lnet/minecraft/client/OptionInstance$ValueSet;Ljava/lang/Object;Lnet/minecraft/client/OptionInstance$ValueUpdateListener;)V"
        ),
        allow = 1,
        slice = @Slice(
            from = @At(
                value = "FIELD",
                opcode = Opcodes.PUTFIELD,
                shift = At.Shift.AFTER,
                target = "Lnet/minecraft/client/Options;gamma:Lnet/minecraft/client/OptionInstance;"
            ),
            to = @At(
                value = "FIELD",
                opcode = Opcodes.PUTFIELD,
                shift = At.Shift.BEFORE,
                target = "Lnet/minecraft/client/Options;guiScale:Lnet/minecraft/client/OptionInstance;"
            )
        )
    )
    private void modifyGuiScaleOption(Args args) {
        args.set(3, new MaxSuppliableIntSliderCallbacks(0, () -> {
            Minecraft minecraftClient = Minecraft.getInstance();
            if (!minecraftClient.isRunning()) {
                return 0x7FFFFFFE;
            }
            return minecraftClient.getWindow().calculateScale(0, minecraftClient.options.forceUnicodeFont().get());
        }, 0x7FFFFFFE));
        OptionInstance.CaptionBasedToString<Integer> textGetter = MixinGameOptions::guiScaleValueToText;
        args.set(2, textGetter);
        args.set(5, (OptionInstance.ValueUpdateListener<Integer>) value -> GuiScaleHandler.onGuiScaleChanged());
    }

    @Unique
    private static Component guiScaleValueToText(Component optionText, Integer value) {
        if (value == 0) {
            return Options.genericValueLabel(optionText, Component.translatable("options.guiScale.auto"));
        } else {
            double scale = ScaleFactorUtil.fromInternalScaleFactor(value.doubleValue());
            return Component.translatable("options.percent_value", optionText, (int) (scale * 100f));
        }
    }
}
