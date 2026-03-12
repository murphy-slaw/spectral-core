package net.funkpla.spectral_core.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.funkpla.spectral_core.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRespawnLogic.class)
public class PlayerRespawnLogicMixin {
  @Unique
  private static final TagKey<Block> unspawnableBlocks =
      TagKey.create(
          BuiltInRegistries.BLOCK.key(), new ResourceLocation(Constants.MOD_ID, "unspawnable"));

  @Inject(
      method = "getOverworldRespawnPos",
      at =
          @At(
              value = "INVOKE_ASSIGN",
              target =
                  "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"),
      cancellable = true)
  private static void checkSpawnBlock(
      ServerLevel level,
      int x,
      int z,
      CallbackInfoReturnable<BlockPos> cir,
      @Local BlockState blockState) {
    if (blockState.is(unspawnableBlocks)) {
      cir.setReturnValue(null);
    }
  }
}
