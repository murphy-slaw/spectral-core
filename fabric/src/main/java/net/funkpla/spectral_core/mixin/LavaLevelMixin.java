package net.funkpla.spectral_core.mixin;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = NoiseBasedChunkGenerator.class, priority = 10000)
public class LavaLevelMixin {
  @ModifyExpressionValue(
      method = "createFluidPicker",
      at = @At(value = "CONSTANT", args = "intValue=-54"))
  private static int change_lava_y_level(int original) {
    return -117;
  }
}
