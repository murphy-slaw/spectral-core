package net.funkpla.spectral_core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import samebutdifferent.ecologics.block.HangingCoconutBlock;

@Mixin(HangingCoconutBlock.class)
public class HangingCoconutBlockMixin {
    @ModifyArg(method = "randomTick", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/util/RandomSource;nextInt" + "(I)I", ordinal = 0), remap = false)
    private int spectral_core$changeGrowthRate(int b) {
        return 5;
    }
}
