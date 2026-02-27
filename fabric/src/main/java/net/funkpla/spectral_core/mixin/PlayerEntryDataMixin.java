package net.funkpla.spectral_core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bennyboops.modid.data.PlayerEntryData;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntryData.class)
public class PlayerEntryDataMixin {
    @WrapOperation(
            method = "<init>",
            at=@At(value="FIELD", target="Lnet/bennyboops/modid/data/PlayerEntryData;entryPos:Lnet/minecraft/world/phys/Vec3;",opcode= Opcodes.PUTFIELD))
    private void oink(PlayerEntryData instance, Vec3 value, Operation<Void> original){
        instance.setEntry(new Vec3(21.5,78,14.5),0,0);
    }

}
