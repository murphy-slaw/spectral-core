package net.funkpla.spectral_core.mixin.small_ships;

import com.talhanation.smallships.world.dockyard.DockyardRecipe;
import com.talhanation.smallships.world.entity.ship.ModBoatTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DockyardRecipe.class)
public class DockyardRecipeMixin {

  @Inject(method = "isWoodSelectable", at = @At("HEAD"), cancellable = true)
  private static void filterForNether(
      Boat.Type type, Player player, CallbackInfoReturnable<Boolean> cir) {
    if (player.level().dimensionType().ultraWarm()) {
      cir.setReturnValue(ModBoatTypes.isNetherWood(type));
    }
  }
}
