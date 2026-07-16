package net.funkpla.spectral_core;

import net.funkpla.spectral_core.item.CopperBucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class CoreItems {
  public static Supplier<Item> COPPER_BUCKET =
      registerItem(
          "copper_bucket",
          new CopperBucketItem(Fluids.EMPTY, (new Item.Properties()).stacksTo(16)));

  public static Supplier<Item> registerItem(String key, Item item) {
    return CommonClass.ITEMS.register(key, () -> item);
  }

  public static void register(){
  }
}
