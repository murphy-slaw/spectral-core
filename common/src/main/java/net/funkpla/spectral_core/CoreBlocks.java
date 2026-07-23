package net.funkpla.spectral_core;

import net.funkpla.spectral_core.block.BedrollBlock;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class CoreBlocks {
  public static Supplier<Block> BEDROLL =
      registerBlock(
          "bedroll", new BedrollBlock());

  public static Supplier<Block> registerBlock(String key, Block block) {
    return CommonClass.BLOCKS.register(key, () -> block);
  }

  public static void register(){
  }
}
