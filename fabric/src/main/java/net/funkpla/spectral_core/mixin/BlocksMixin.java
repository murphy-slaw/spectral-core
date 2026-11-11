package net.funkpla.spectral_core.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.funkpla.spectral_core.config.CoreConfig;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Blocks.class)
public class BlocksMixin {

  @Unique private static CoreConfig CONFIG;

  @ModifyArg(
      method = "<clinit>",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/world/level/block/CaveVines;emission(I)Ljava/util/function/ToIntFunction;"))
  private static int reduceGlowberryLight(int berries) {
    if (CONFIG == null) {
        AutoConfig.register(CoreConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(CoreConfig.class).getConfig();
    }
    return CONFIG.glowBerryLightLevel;
  }
}
