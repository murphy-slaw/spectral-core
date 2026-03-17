package net.funkpla.spectral_core.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladiumcore.event.EventResult;
import net.venturecraft.gliders.common.GliderEvents;
import net.venturecraft.gliders.common.item.GliderItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(GliderEvents.class)
public class GliderEventsMixin {
  @Inject(
      method = "anvilUpdate",
      at =
          @At(
              target =
                  "Lnet/venturecraft/gliders/common/item/GliderItem;isValidRepairItem"
                      + "(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z",
              value = "INVOKE_ASSIGN"), cancellable = true)
  private void cancelRepair(
          Player player,
          ItemStack left,
          ItemStack right,
          String name,
          AtomicInteger cost,
          AtomicInteger materialCost,
          AtomicReference<ItemStack> output,
          CallbackInfoReturnable<EventResult> cir, @Local(name = "gliderItem") GliderItem gliderItem) {
      if (gliderItem.isValidRepairItem(left, right)) {
          cir.setReturnValue(EventResult.pass());
      }
  }
}
