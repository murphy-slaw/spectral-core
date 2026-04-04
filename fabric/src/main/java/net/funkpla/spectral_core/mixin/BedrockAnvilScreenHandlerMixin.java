package net.funkpla.spectral_core.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.dafuqs.spectrum.inventories.BedrockAnvilScreenHandler;
import folk.sisby.tinkerers_smithing.TinkerersSmithing;
import folk.sisby.tinkerers_smithing.recipe.ShapelessRepairRecipe;
import java.util.Map;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BedrockAnvilScreenHandler.class)
public abstract class BedrockAnvilScreenHandlerMixin extends ItemCombinerMenu{
	@Shadow
	private int repairItemCount;
    @Final
    @Shadow
	private DataSlot levelCost;

	public BedrockAnvilScreenHandlerMixin(@Nullable MenuType<?> type, int syncId, Inventory playerInventory,
                                   ContainerLevelAccess access) {
		super(type, syncId, playerInventory, access);
	}

	@ModifyExpressionValue(method = "createResult", at = @At(value = "INVOKE", target = "Lde/dafuqs/spectrum" +
            "/inventories/BedrockAnvilScreenHandler;getNextCost(I)I"))
	private int noLevelsNoWork(int original) {
		return this.levelCost.get() == 0 ? (original - 1) / 2 : original;
	}

	@ModifyExpressionValue(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item" +
            "/Item;isValidRepairItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
	private boolean overrideRepairMaterials(boolean original) {
		for (CraftingRecipe recipe : this.player.level().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING)) {
			if (recipe instanceof ShapelessRepairRecipe srr && this.getSlot(AnvilMenu.INPUT_SLOT
            ).getItem().is(srr.baseItem) && srr.addition.test(this.getSlot(AnvilMenu.ADDITIONAL_SLOT).getItem())) {
				return true;
			}
		}
		return false;
	}

	@ModifyVariable(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item" +
            "/ItemStack;setDamageValue(I)V"
            , ordinal = 0), name = "enchantmentLevelCost")
	private int unitRepairNoLevels(int original) {
		return original - 1;
	}

	@ModifyVariable(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;" +
            "setDamageValue(I)V", ordinal = 1), name = "enchantmentLevelCost")
	private int combineRepairNoLevels(int original) {
		this.repairItemCount = -1;
		return original - 2;
	}

	@ModifyExpressionValue(method = "mayPickup", at = @At(value = "INVOKE", target ="Lnet/minecraft/world/inventory" +
            "/DataSlot;get()I", ordinal = 0))
	private int allowTakingFreeRepairs(int original) {
		return original == 0 && this.repairItemCount != 0 ? 1 : original;
	}

	@ModifyVariable(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory" +
            "/DataSlot;set" +
            "(I)V", ordinal = 2, shift = At.Shift.AFTER), name = "enchantmentLevelCost")
	private int allowFreeRepairs(int original) {
		if (original == 0 && this.repairItemCount != 0) {
			this.levelCost.set(0); // Remove RepairCost cost
			return 1;
		} else {
			return original;
		}
	}

	@Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is" +
            "(Lnet/minecraft/world/item/Item;)Z",
             ordinal = 2), cancellable = true)
	private void applyDeworkMaterial(CallbackInfo ci) {
		ItemStack base = this.getSlot(AnvilMenu.INPUT_SLOT).getItem();
		ItemStack ingredient = this.getSlot(AnvilMenu.ADDITIONAL_SLOT).getItem();
		if (ingredient.is(TinkerersSmithing.DEWORK_INGREDIENTS) && base.getBaseRepairCost() > 0) {
			ItemStack result = base.copy();
			this.repairItemCount = 0;
			do {
				result.setRepairCost(((result.getBaseRepairCost() + 1) / 2) - 1);
				this.repairItemCount++;
			} while (result.getBaseRepairCost() > 0 && this.repairItemCount < ingredient.getCount());
			this.getSlot(AnvilMenu.RESULT_SLOT).set(result);
			this.levelCost.set(0);
			this.broadcastChanges();
			ci.cancel();
		}
	}

	@Unique
	private int getSRCost(Map<Enchantment, Integer> base, Map<Enchantment, Integer> ingredient) {
		return ingredient.entrySet().stream().map(entry -> {
			Enchantment enchantment = entry.getKey();
			int level = entry.getValue();
			int baseLevel = base.getOrDefault(enchantment, 0);
			int resultLevel = baseLevel == level ? level + 1 : Math.max(level, baseLevel);
			int rarityCost = switch (enchantment.getRarity()) {
				case COMMON -> 1;
				case UNCOMMON -> 2;
				case RARE -> 4;
				case VERY_RARE -> 8;
			};
			return rarityCost * resultLevel;
		}).reduce(0, Integer::sum);
	}

	@Unique
	private boolean doSwapEnchantments(Map<Enchantment, Integer> base, Map<Enchantment, Integer> ingredient) {
		return !(this.getSlot(AnvilMenu.RESULT_SLOT).getItem().is(Items.ENCHANTED_BOOK)) && getSRCost(base,
                ingredient) > getSRCost(ingredient, base);
	}

	@ModifyVariable(method = "createResult", at = @At(value = "INVOKE_ASSIGN", target =
            "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantments" +
                    "(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Map;",
            ordinal = 1), name = "enchantmentLevelMap")
	private Map<Enchantment, Integer> orderlessCombineSwapBaseTable(Map<Enchantment, Integer> base) {
		Map<Enchantment, Integer> ingredient =
                EnchantmentHelper.getEnchantments(this.getSlot(AnvilMenu.ADDITIONAL_SLOT).getItem());
		return doSwapEnchantments(base, ingredient) ? ingredient : base;
	}

	@ModifyVariable(method = "createResult", at = @At(value = "INVOKE_ASSIGN", target =
            "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantments" +
                    "(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Map;",
            ordinal = 1), name = "currentEnchantments")
	private Map<Enchantment, Integer> orderlessCombineSwapIngredientTable(Map<Enchantment, Integer> ingredient) {
		Map<Enchantment, Integer> base =
                EnchantmentHelper.getEnchantments(this.getSlot(AnvilMenu.INPUT_SLOT).getItem());
		return doSwapEnchantments(base, ingredient) ? base : ingredient;
	}
}
