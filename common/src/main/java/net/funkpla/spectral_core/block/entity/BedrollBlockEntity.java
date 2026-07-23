package net.funkpla.spectral_core.block.entity;

import com.illusivesoulworks.comforts.common.block.entity.BaseComfortsBlockEntity;
import net.funkpla.spectral_core.CoreBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BedrollBlockEntity extends BaseComfortsBlockEntity {
    public BedrollBlockEntity(BlockPos pos, BlockState state) {
        super(CoreBlockEntityTypes.BEDROLL.get(), pos, state);
    }

    @Override
    public @NotNull Component getName() {
        return this.name != null ? this.name : Component.translatable("block.spectral_core.bedroll");
    }
}
