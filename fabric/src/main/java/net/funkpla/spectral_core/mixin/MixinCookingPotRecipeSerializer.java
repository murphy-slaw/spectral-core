package net.funkpla.spectral_core.mixin;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.funkpla.spectral_core.Constants;
import net.funkpla.spectral_core.NbtDataOwner;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

@Mixin(CookingPotRecipe.Serializer.class)
public abstract class MixinCookingPotRecipeSerializer {
  @Unique private static final String DATA_TAG = "data";

  @Inject(
      method =
          "fromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;)Lvectorwing/farmersdelight/common/crafting/CookingPotRecipe;",
      at = @At(value = "RETURN", remap = false),
      cancellable = true)
  private static void getRecipesNbtData(
      ResourceLocation recipeId,
      JsonObject json,
      CallbackInfoReturnable<CookingPotRecipe> cir,
      @Local(name = "groupIn") String groupIn,
      @Local(name = "inputItemsIn") NonNullList<Ingredient> inputItemsIn,
      @Local(name = "tabIn") CookingPotRecipeBookTab tabIn,
      @Local(name = "outputIn") ItemStack outputIn,
      @Local(name = "container") ItemStack container,
      @Local(name = "experienceIn") float experienceIn,
      @Local(name = "cookTimeIn") int cookTimeIn) {

    CompoundTag currentNbtData = null;

    JsonObject result = json.getAsJsonObject("result");

    if (result.has(DATA_TAG)) {
      String nbtString;

      if (GsonHelper.isStringValue(result, DATA_TAG)) {
        nbtString = result.get(DATA_TAG).getAsString();
      } else {
        nbtString = GsonHelper.getAsJsonObject(result, DATA_TAG).toString();
      }

      try {
        currentNbtData = new TagParser(new StringReader(nbtString)).readStruct();
      } catch (CommandSyntaxException e) {
        Constants.LOG.error("Failed to parse custom NBT data", e);
      }

      json.remove("data");
      CookingPotRecipe recipe =
          new CookingPotRecipe(
              recipeId,
              groupIn,
              tabIn,
              inputItemsIn,
              outputIn,
              container,
              experienceIn,
              cookTimeIn);
      ((NbtDataOwner) recipe).spectral_core$setCurrentNbtData(currentNbtData);
      cir.setReturnValue(recipe);
    }
  }
}
