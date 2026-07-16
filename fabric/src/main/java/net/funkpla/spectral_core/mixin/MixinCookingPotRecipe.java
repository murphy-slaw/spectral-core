package net.funkpla.spectral_core.mixin;

import ca.sync.nbtrecipes.utils.IItemStack;
import net.funkpla.spectral_core.NbtDataOwner;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

@Mixin(CookingPotRecipe.class)
public abstract class MixinCookingPotRecipe implements NbtDataOwner {

  @Unique CompoundTag currentNbtData = null;

  @Inject(method = "getResultItem", at = @At("HEAD"), cancellable = true)
  private void setRecipesNbtData(RegistryAccess access, CallbackInfoReturnable<ItemStack> cir) {
    ItemStack stack = ((CookingRecipeOutputAccessor) this).getOutputStack();

    if (currentNbtData != null) {
      CompoundTag nbtData = currentNbtData.copy();
      ((IItemStack) (Object) stack).setRawTag(nbtData);
    }

    cir.setReturnValue(stack);
  }

    @Unique
  @Override
  public void spectral_core$setCurrentNbtData(CompoundTag tag) {
    currentNbtData = tag;
  }
}
