package net.funkpla.spectral_core.mixin.small_ships;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.talhanation.smallships.world.entity.ship.ModBoatTypes;
import com.talhanation.smallships.world.entity.ship.Ship;
import net.minecraft.tags.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Ship.class)
public abstract class ShipMixin {

  @WrapMethod(method = "floatUp", remap = false)
  private void floatUpInLava(Operation<Void> original) {
    Ship ship = (Ship) (Object) this;
    ship.fireImmune();
    if (ModBoatTypes.isNetherWood(ship.getVariant())) {
      if (ship.isEyeInFluid(FluidTags.WATER) || ship.isEyeInFluid(FluidTags.LAVA)) {
        ship.setDeltaMovement(ship.getDeltaMovement().add(0.0, 0.2, 0.0));
      }
    }
    original.call();
  }

  @WrapOperation(method="controlShip", remap = false, at=@At(value="INVOKE",target="Lcom/talhanation/smallships/world/entity/ship/Ship;isInWater()Z"))
    private boolean isInWaterOrLava(Ship instance, Operation<Boolean> original){
      return true;
  }

  @WrapOperation(method="tick", remap=false, at=@At(value= "INVOKE", target="Lcom/talhanation/smallships/world/entity/ship/Ship;setDamage(F)V"))
    private void hackAroundDamage(Ship ship, float v, Operation<Void> original, @Local(name = "hullDamageBeforeVanillaTick") float hullDamageBeforeVanillaTick){
      if (hullDamageBeforeVanillaTick == ship.getDamage() + 1) ship.setDamage(hullDamageBeforeVanillaTick);
  }
}
