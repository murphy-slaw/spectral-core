package net.funkpla.spectral_core.mixin.small_ships;

import com.talhanation.smallships.client.gui.screens.inventory.DockyardScreen;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DockyardScreen.class)
public interface DockyardScreenInvoker {
    @Invoker("setWoodType")
    void spectral_core$setWoodTypeInvoker(Boat.Type type);
}
