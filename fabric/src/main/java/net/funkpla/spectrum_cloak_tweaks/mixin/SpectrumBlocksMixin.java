package net.funkpla.spectrum_cloak_tweaks.mixin;

import de.dafuqs.spectrum.blocks.conditional.CloakedOreBlock;
import de.dafuqs.spectrum.registries.SpectrumAdvancements;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpectrumBlocks.class)
public class SpectrumBlocksMixin {

  @Redirect(
      method = "<clinit>",
      at =
          @At(
              args = "class=de/dafuqs/spectrum/blocks/conditional/CloakedOreBlock",
              value = "NEW",
              ordinal = 4))
  @Unique
  private static CloakedOreBlock replacePalteria(
      BlockBehaviour.Properties settings,
      UniformInt uniformIntProvider,
      ResourceLocation cloakAdvancementIdentifier,
      BlockState cloakBlockState) {
    return new CloakedOreBlock(
        FabricBlockSettings.copyOf(Blocks.END_STONE).strength(3.0F, 3.0F).requiresTool(),
        UniformInt.of(2, 4),
        SpectrumAdvancements.REVEAL_PALTAERIA,
        Blocks.DEEPSLATE.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Z));
  }
}
