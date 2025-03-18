package com.wetwater.washwater.mixin;

import com.wetwater.washwater.FluidManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static net.minecraft.world.level.block.PointedDripstoneBlock.TIP_DIRECTION;

@Mixin(PointedDripstoneBlock.class)
public class MixinPointedDripstoneBlock {

    //Simple infinite fluid feature removal
/*    @Redirect(method = "randomTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/PointedDripstoneBlock;maybeFillCauldron(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;F)V"
            )
    )
    public void noMoreInfinity(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, float f) {
        //haha, no code here
    }*/


    @Redirect(method = "randomTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/PointedDripstoneBlock;maybeFillCauldron(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;F)V"
            )
    )
    public void newFunctionality(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, float f) {
        //Replace fluid duplication with fluid pass-through
        int predefinedDripAmount = 10;
        float probability = 0.4f;
        if (f < probability) {
            if ((findAndRemoveFluidAbove(serverLevel, blockPos, blockState, predefinedDripAmount).get()) > 0) {
                FluidManager.addVolume(serverLevel, blockPos.below(), 10);
            }
        }
    }

    private static Optional<Integer> findAndRemoveFluidAbove(Level level, BlockPos blockPos, BlockState blockState, int amount) {
        return !isStalactiteCustom(blockState) ? Optional.empty() : findRootBlockCustom(level, blockPos, blockState, 11).map((blockPosx) -> {
            int volume = FluidManager.getVolume(level, blockPosx.above());
            FluidManager.setVolume((ServerLevel) level, blockPosx.above(), volume - amount);
            return volume;
        });
    }

    private static boolean isStalactiteCustom(BlockState blockState) {
        return isPointedDripstoneWithDirectionCustom(blockState, Direction.DOWN);
    }

    private static boolean isPointedDripstoneWithDirectionCustom(BlockState blockState, Direction direction) {
        return blockState.is(Blocks.POINTED_DRIPSTONE) && blockState.getValue(TIP_DIRECTION) == direction;
    }

    private static Optional<BlockPos> findRootBlockCustom(Level level, BlockPos blockPos, BlockState blockState, int i) {
        Direction direction = (Direction)blockState.getValue(TIP_DIRECTION);
        BiPredicate<BlockPos, BlockState> biPredicate = (blockPosx, blockStatex) -> {
            return blockStatex.is(Blocks.POINTED_DRIPSTONE) && blockStatex.getValue(TIP_DIRECTION) == direction;
        };
        return findBlockVerticalCustom(level, blockPos, direction.getOpposite().getAxisDirection(), biPredicate, (blockStatex) -> {
            return !blockStatex.is(Blocks.POINTED_DRIPSTONE);
        }, i);
    }

    private static Optional<BlockPos> findBlockVerticalCustom(LevelAccessor levelAccessor, BlockPos blockPos, Direction.AxisDirection axisDirection, BiPredicate<BlockPos, BlockState> biPredicate, Predicate<BlockState> predicate, int i) {
        Direction direction = Direction.get(axisDirection, Direction.Axis.Y);
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();

        for(int j = 1; j < i; ++j) {
            mutableBlockPos.move(direction);
            BlockState blockState = levelAccessor.getBlockState(mutableBlockPos);
            if (predicate.test(blockState)) {
                return Optional.of(mutableBlockPos.immutable());
            }

            if (levelAccessor.isOutsideBuildHeight(mutableBlockPos.getY()) || !biPredicate.test(mutableBlockPos, blockState)) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
