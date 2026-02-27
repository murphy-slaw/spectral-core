package net.funkpla.spectral_core.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.items.magic_items.PaintbrushItem;
import net.bennyboops.modid.block.SuitcaseBlock;
import net.bennyboops.modid.block.entity.SuitcaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SuitcaseBlock.class)
public class SuitcaseBlockMixin {
  @Inject(
      method = "use",
      at =
          @At(
              value = "INVOKE_ASSIGN",
              target =
                  "Lnet/bennyboops/modid/block/entity/SuitcaseBlockEntity;"
                      + "getBoundKeystoneName()Ljava/lang/String;"),
      cancellable = true)
  private void useWithPaintbrush(
      BlockState state,
      Level world,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hit,
      CallbackInfoReturnable<InteractionResult> cir,
      @Local(name = "heldItem") ItemStack heldItem,
      @Local(name = "boundKeystone") String boundKeystone,
      @Local(name = "suitcase") SuitcaseBlockEntity suitcase) {
    if (heldItem.getItem() instanceof PaintbrushItem) {

      String keystoneName = player.getStringUUID();
      // Binding logic
      if (boundKeystone == null) {
        suitcase.bindKeystone(keystoneName);
        world.playSound(
            null, pos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.BLOCKS, 2.0F, 0.0F);
        player.displayClientMessage(
            Component.translatable("item.spectral_seas.paintbrush.bound"), true);
        cir.setReturnValue(InteractionResult.SUCCESS);
      }
      if (boundKeystone != null && boundKeystone.equals(keystoneName)) {
        boolean newLockState = !suitcase.isLocked();
        suitcase.setLocked(newLockState);
        world.playSound(
            null,
            pos,
            newLockState ? SoundEvents.IRON_DOOR_CLOSE : SoundEvents.IRON_DOOR_OPEN,
            SoundSource.BLOCKS,
            0.3F,
            2.0F);
        player.displayClientMessage(
            newLockState
                ? Component.translatable("item.spectral_seas.paintbrush.locked")
                : Component.translatable("item.spectral_seas.paintbrush.unlocked"),
            true);
        cir.setReturnValue(InteractionResult.SUCCESS);
      }
    }
  }
}
