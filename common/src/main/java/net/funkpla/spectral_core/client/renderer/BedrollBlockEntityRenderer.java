package net.funkpla.spectral_core.client.renderer;

import com.illusivesoulworks.comforts.client.renderer.BaseComfortsBlockEntityRenderer;
import net.funkpla.spectral_core.block.entity.BedrollBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BedrollBlockEntityRenderer extends BaseComfortsBlockEntityRenderer<BedrollBlockEntity> {
    public BedrollBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super(ctx, "bedroll", BaseComfortsBlockEntityRenderer.SLEEPING_BAG_HEAD,
                BaseComfortsBlockEntityRenderer.SLEEPING_BAG_FOOT);
    }
}
