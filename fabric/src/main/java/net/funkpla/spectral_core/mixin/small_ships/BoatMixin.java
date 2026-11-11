package net.funkpla.spectral_core.mixin.small_ships;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.talhanation.smallships.world.entity.ship.ModBoatTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Boat.class)
public class BoatMixin {
  @WrapOperation(
      method = "checkInWater",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
  private boolean isWaterOrLava(
      FluidState fluidState, TagKey<Fluid> tag, Operation<Boolean> original) {
    Boat boat = (Boat) (Object) this;
    if (ModBoatTypes.isNetherWood(boat.getVariant())) {
      return fluidState.is(tag) || fluidState.is(FluidTags.LAVA);
    }
    return original.call(fluidState, tag);
  }
}
