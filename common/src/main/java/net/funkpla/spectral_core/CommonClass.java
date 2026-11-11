package net.funkpla.spectral_core;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.funkpla.spectral_core.config.CoreConfig;
import net.funkpla.spectral_core.platform.Services;
import net.funkpla.spectral_core.platform.registration.RegistrationProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CommonClass {

  public static final RegistrationProvider<Item> ITEMS =
      RegistrationProvider.get(BuiltInRegistries.ITEM, Constants.MOD_ID);
  public static final RegistrationProvider<Block> BLOCKS =
      RegistrationProvider.get(BuiltInRegistries.BLOCK, Constants.MOD_ID);
  public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
      RegistrationProvider.get(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
  public static CoreConfig CONFIG;

  public static ResourceLocation locate(String path) {
    return new ResourceLocation(Constants.MOD_ID, path);
  }

  public static void init() {
    CONFIG = getConfig();
    if (Services.PLATFORM.isModLoaded("spectral_core")) {

      Constants.LOG.info("Spectral Core loading…");
    }
    CoreItems.register();
    CoreBlocks.register();
    CoreBlockEntityTypes.register();
  }

  public static CoreConfig getConfig(){
      if (AutoConfig.getConfigHolder(CoreConfig.class) == null)
          AutoConfig.register(CoreConfig.class, JanksonConfigSerializer::new);
      return AutoConfig.getConfigHolder(CoreConfig.class).getConfig();
  }
}
