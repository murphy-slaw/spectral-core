package net.funkpla.spectral_core.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bennyboops.modid.world.RuntimeWorldConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RuntimeWorldConfig.class)
public class RuntimeWorldConfigMixin {
  @WrapMethod(remap = false, method = "shouldTickTime")
  private boolean foo(Operation<Boolean> original) {
    return true;
  }

  @WrapOperation(
      remap = false,
      method = "<init>",
      at =
          @At(
              value = "FIELD",
              target = "Lnet/bennyboops/modid/world" + "/RuntimeWorldConfig;sunnyTime:I",
              opcode = Opcodes.PUTFIELD))
  private void moose(RuntimeWorldConfig instance, int value, Operation<Void> original) {
    instance.setSunny(0);
  }
}
