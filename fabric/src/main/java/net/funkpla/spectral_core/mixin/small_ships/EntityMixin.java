package net.funkpla.spectral_core.mixin.small_ships;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.talhanation.smallships.world.entity.ship.ModBoatTypes;
import com.talhanation.smallships.world.entity.ship.Ship;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class EntityMixin {
  @WrapMethod(method = "fireImmune")
  private boolean fireImmuneTweaked(Operation<Boolean> original) {
    if ((Entity) (Object) this instanceof Ship ship) {
      if (ModBoatTypes.isNetherWood(ship.getVariant())) {
        return true;
      }
    }
    return original.call();
  }
}
