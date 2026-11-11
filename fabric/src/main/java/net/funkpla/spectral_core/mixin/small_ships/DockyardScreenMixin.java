package net.funkpla.spectral_core.mixin.small_ships;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.talhanation.smallships.client.gui.screens.inventory.DockyardScreen;
import com.talhanation.smallships.world.entity.ship.ModBoatTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DockyardScreen.class)
public class DockyardScreenMixin {

  /** Replace Oak with Crimson as default wood type in ultrawarm dimensions */
  @WrapOperation(
      method = "updateWidgetVisibility",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lcom/talhanation/smallships/client/gui/screens/inventory/DockyardScreen;setWoodType(Lnet/minecraft/world/entity/vehicle/Boat$Type;)V"))
  private void maybeNotOak(DockyardScreen instance, Boat.Type type, Operation<Void> original) {
    if (Minecraft.getInstance().level.dimensionType().ultraWarm()) {
      ((DockyardScreenInvoker) instance)
          .spectral_core$setWoodTypeInvoker(Boat.Type.byName(ModBoatTypes.CRIMSON));
    } else original.call(instance, type);
  }
}
