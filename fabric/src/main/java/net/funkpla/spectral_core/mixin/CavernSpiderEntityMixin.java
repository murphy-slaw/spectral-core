package net.funkpla.spectral_core.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import dev.chybx.spideroverhaul.entity.CavernSpiderEntity;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CavernSpiderEntity.class)
public abstract class CavernSpiderEntityMixin {
  @Definition(
      id = "getTypeDrop",
      method =
          "Ldev/chybx/spideroverhaul/entity/CavernSpiderEntity;getTypeDrop()"
              + "Lnet/minecraft/world/item/ItemStack;")
  @Expression("? = ?.getTypeDrop()")
  @Inject(
      method = "dropFromLootTable",
      cancellable = true,
      remap = false,
      at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER))
  private void stopDrops(DamageSource damageSource, boolean hitByPlayer, CallbackInfo ci) {
    ci.cancel();
  }
}
