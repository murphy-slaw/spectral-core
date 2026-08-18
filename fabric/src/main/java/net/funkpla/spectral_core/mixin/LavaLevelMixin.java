package net.funkpla.spectral_core.mixin;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.funkpla.spectral_core.CommonClass;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NoiseBasedChunkGenerator.class)
public class LavaLevelMixin {
    @ModifyExpressionValue(method = "createFluidPicker", at = @At(value = "CONSTANT", args = "intValue=-54"))
    private static int change_lava_y_level(int original) {
        return CommonClass.CONFIG.lavaLevel;
    }
}
