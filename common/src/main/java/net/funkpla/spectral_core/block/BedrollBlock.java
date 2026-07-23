package net.funkpla.spectral_core.block;

import com.illusivesoulworks.comforts.common.block.SleepingBagBlock;
import com.illusivesoulworks.comforts.common.block.entity.BaseComfortsBlockEntity;
import net.funkpla.spectral_core.CoreBlockEntityTypes;
import net.funkpla.spectral_core.block.entity.BedrollBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class BedrollBlock extends SleepingBagBlock {
    private static final DyeColor DYE_COLOR = DyeColor.WHITE;

    public BedrollBlock() {
        super(DYE_COLOR);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new BedrollBlockEntity(pos, state);
    }

    @Override
    public BlockEntityType<? extends BaseComfortsBlockEntity> getBlockEntityType() {
        return CoreBlockEntityTypes.BEDROLL.get();
    }
}
