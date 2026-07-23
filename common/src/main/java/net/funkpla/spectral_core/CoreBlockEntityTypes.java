package net.funkpla.spectral_core;

import net.funkpla.spectral_core.block.entity.BedrollBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class CoreBlockEntityTypes {

    public static final Supplier<BlockEntityType<BedrollBlockEntity>> BEDROLL = registerBlockEntityType(
            "bedroll",
            BlockEntityType.Builder.of(BedrollBlockEntity::new, CoreBlocks.BEDROLL.get()).build(null));

    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntityType(String key,
                                                                                               BlockEntityType<T> blockEntityType) {
        return CommonClass.BLOCK_ENTITY_TYPES.register(key, () -> blockEntityType);
    }

    public static void register() {

    }
}