package net.funkpla.spectral_core;

import net.fabricmc.api.ClientModInitializer;
import net.funkpla.spectral_core.client.renderer.BedrollBlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class SpectralCoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(CoreBlockEntityTypes.BEDROLL.get(), BedrollBlockEntityRenderer::new);
    }
}
