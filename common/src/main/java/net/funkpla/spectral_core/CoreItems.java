package net.funkpla.spectral_core;

import net.funkpla.spectral_core.item.BedrollItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class CoreItems {
    public static Supplier<Item> BEDROLL = registerItem("bedroll",
            new BedrollItem(CoreBlocks.BEDROLL.get()));

    public static Supplier<Item> registerItem(String key, Item item) {
        return CommonClass.ITEMS.register(key, () -> item);
    }

    public static void register() {
    }
}
