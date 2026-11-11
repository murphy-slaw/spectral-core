package net.funkpla.spectral_core.mixin;

import io.github.uhq_games.regions_unexplored.block.RuBlocks;
import java.util.function.ToIntFunction;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.funkpla.spectral_core.config.CoreConfig;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(RuBlocks.class)
public class RUBlocksMixin {

  @Unique private static CoreConfig CONFIG;

  @ModifyArg(
      method = "<clinit>",
      remap = false,
      at =
          @At(
              value = "INVOKE",
              ordinal = 5,
              target =
                  "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;lightLevel(Ljava/util/function/ToIntFunction;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;"))
  private static ToIntFunction<BlockState> reduceDropleafLight(
      ToIntFunction<BlockState> lightEmission) {
    if (CONFIG == null) {
      if (AutoConfig.getConfigHolder(CoreConfig.class) == null)
        AutoConfig.register(CoreConfig.class, JanksonConfigSerializer::new);
      CONFIG = AutoConfig.getConfigHolder(CoreConfig.class).getConfig();
    }
    return (s -> CONFIG.dropleafLightLevel);
  }

  @ModifyArg(
      method = "<clinit>",
      remap = false,
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;lightLevel"
                      + "(Ljava/util/function/ToIntFunction;)"
                      + "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;"),
      slice =
          @Slice(
              from =
                  @At(
                      value = "INVOKE",
                      target =
                          "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;lightLevel"
                              + "(Ljava/util/function/ToIntFunction;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                      ordinal = 6),
              to =
                  @At(
                      value = "INVOKE",
                      target =
                          "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;lightLevel"
                              + "(Ljava/util/function/ToIntFunction;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
                      ordinal = 9)))
  private static ToIntFunction<BlockState> reduceBioshroomLight(
      ToIntFunction<BlockState> lightEmission) {
    if (CONFIG == null) {
      if (AutoConfig.getConfigHolder(CoreConfig.class) == null)
        AutoConfig.register(CoreConfig.class, JanksonConfigSerializer::new);
      CONFIG = AutoConfig.getConfigHolder(CoreConfig.class).getConfig();
    }
    return (s -> CONFIG.bioshroomLightLevel);
  }

  @ModifyArg(
      method = "<clinit>",
      remap = false,
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;lightLevel"
                      + "(Ljava/util/function/ToIntFunction;)"
                      + "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
              ordinal = 9))
  private static ToIntFunction<BlockState> reduceGlowingBioshroomBlockLight(
      ToIntFunction<BlockState> lightEmission) {
    if (CONFIG == null) {
      if (AutoConfig.getConfigHolder(CoreConfig.class) == null)
        AutoConfig.register(CoreConfig.class, JanksonConfigSerializer::new);
      CONFIG = AutoConfig.getConfigHolder(CoreConfig.class).getConfig();
    }
    return (s -> CONFIG.bioshroomBlockLightLevel);
  }
}
