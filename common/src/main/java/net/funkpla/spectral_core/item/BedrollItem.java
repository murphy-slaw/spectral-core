package net.funkpla.spectral_core.item;

import com.illusivesoulworks.comforts.common.block.BaseComfortsBlock;
import com.illusivesoulworks.comforts.common.item.SleepingBagItem;
import com.illusivesoulworks.comforts.platform.Services;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class BedrollItem extends SleepingBagItem {

    public BedrollItem(Block block) {
        super(block);
    }

    @Override
    public @NotNull InteractionResult useOn(@Nonnull UseOnContext context) {
        final Player player = context.getPlayer();

        if (player instanceof ServerPlayer serverPlayer) {
            Either<Player.BedSleepingProblem, Unit> result = BaseComfortsBlock.trySleep(serverPlayer,
                    context.getClickedPos().above(), true);
            return result.map(bedSleepingProblem -> {
                final Component text = switch (bedSleepingProblem) {
                    case NOT_POSSIBLE_NOW -> Component.translatable("block.minecraft.bed.no_sleep");
                    case TOO_FAR_AWAY -> Component.translatable("block.comforts.sleeping_bag.too_far_away");
                    default -> bedSleepingProblem.getMessage();
                };

                if (text != null) {
                    player.displayClientMessage(text, true);
                }
                return InteractionResult.FAIL;
            }, unit -> {
                final InteractionResult interactionResult = this.place(new BlockPlaceContext(context));
                if (interactionResult.consumesAction()) {
                    Services.SLEEP_EVENTS.sendPlaceBagPacket(serverPlayer, context);
                    final BlockPos pos = context.getClickedPos().above();
                    Services.SLEEP_EVENTS.getSleepData(player).ifPresent(data -> data.setAutoSleepPos(pos));
                    Services.SLEEP_EVENTS.sendAutoSleepPacket(serverPlayer, pos);
                }
                return interactionResult;
            });
        }
        return InteractionResult.CONSUME;
    }
}
