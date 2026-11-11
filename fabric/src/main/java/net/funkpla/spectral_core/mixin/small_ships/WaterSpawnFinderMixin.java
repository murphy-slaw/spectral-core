package net.funkpla.spectral_core.mixin.small_ships;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.talhanation.smallships.world.dockyard.WaterSpawnFinder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaterSpawnFinder.class)
public class WaterSpawnFinderMixin {
    @WrapOperation(method="isWaterSurface", at=@At(value="INVOKE", target="Lnet/minecraft/world/level/material/Fluid;isSame(Lnet/minecraft/world/level/material/Fluid;)Z"))
    private static boolean isWaterOrLavaSurface(Fluid fluid1, Fluid fluid, Operation<Boolean> original){
        return fluid1.isSame(fluid) || fluid1.isSame(Fluids.LAVA);
    }
    @WrapOperation(method="isWaterColumn", at=@At(value="INVOKE", target="Lnet/minecraft/world/level/material/Fluid;" +
            "isSame(Lnet/minecraft/world/level/material/Fluid;)Z"))
    private static boolean isWaterOrLavaColumn(Fluid fluid1, Fluid fluid, Operation<Boolean> original){
        return fluid1.isSame(fluid) || fluid1.isSame(Fluids.LAVA);
    }
}
