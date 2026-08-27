package net.rosemods.betteruiscale;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;

import java.util.Optional;
import java.util.function.IntSupplier;

@Environment(value = EnvType.CLIENT)
public record MaxSuppliableIntSliderCallbacks(int minInclusive, IntSupplier maxSupplier,
                                              int encodableMaxInclusive) implements OptionInstance.IntRangeBase {
    @Override
    public Optional<Integer> validateValue(Integer integer) {
        return Optional.of(Mth.clamp(integer, this.minInclusive(), this.maxInclusive()));
    }

    @Override
    public int maxInclusive() {
        return this.maxSupplier.getAsInt();
    }

    @Override
    public Codec<Integer> codec() {
        return ExtraCodecs.intRange(this.minInclusive, this.encodableMaxInclusive + 1);
    }
}
