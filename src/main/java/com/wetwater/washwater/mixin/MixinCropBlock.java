package com.wetwater.washwater.mixin;

import com.wetwater.washwater.block.entity.CropBlockEntity;
import com.wetwater.washwater.block.entity.FathomlessFluidChasmBlockEntity;
import com.wetwater.washwater.block.entity.ModBlockEntities;
import com.wetwater.washwater.util.CropUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DaylightDetectorBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

@Mixin(CropBlock.class)
public abstract class MixinCropBlock extends BushBlock implements BonemealableBlock, EntityBlock {


    public MixinCropBlock(Properties properties) {
        super(properties);
    }

    @Shadow
    protected abstract int getAge(BlockState blockState);

    @Shadow
    public abstract int getMaxAge();

    @Shadow
    protected static float getGrowthSpeed(Block block, BlockGetter blockGetter, BlockPos blockPos) {
        throw new AssertionError();
    }

    @Shadow
    public abstract BlockState getStateForAge(int i);

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if (serverLevel.getRawBrightness(blockPos, 0) >= 9) {
            int i = this.getAge(blockState);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(this, serverLevel, blockPos);
                if (random.nextInt((int)(25.0F / f) + 1) == 0) {
                    //System.out.println("time of day: " + serverLevel.getTimeOfDay(1f));
                    System.out.println("Sunlight ratio: " + CropUtil.getTimeRelativeInsolation(serverLevel));
                    //System.out.println("Sun angle: " + serverLevel.getSunAngle(1f));
                    System.out.println("Sunlight: " + CropUtil.getSunLight(serverLevel, blockPos));
                    //System.out.println("Is getting sunlight: " + CropUtil.isGettingDirectSunlight(serverLevel, blockPos));
                    //System.out.println("Downfall: " + CropUtil.getDownfall(serverLevel, blockPos));
                    System.out.println("Temp: " + CropUtil.getBiomeTemperature(serverLevel, blockPos));
                    System.out.println("BiomeSun: " + CropUtil.getBiomeInsolationCoefficient(serverLevel, blockPos));
                    System.out.println("Current insolation: " + CropUtil.getCurrentInsolation(serverLevel, blockPos));
                    //serverLevel.setBlock(blockPos, this.getStateForAge(i + 1), 2);
                }
            }
        }

    }

/*    @Unique
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DaylightDetectorBlockEntity(blockPos, blockState);
    }

    @Unique
    private static void tickEntity(Level level, BlockPos blockPos, BlockState blockState, DaylightDetectorBlockEntity daylightDetectorBlockEntity) {
            System.out.println("Ticked the entity");

    }*/

/*    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CropBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.FATHOMLESS_FLUID_CHASM, FathomlessFluidChasmBlockEntity::tick);
    }*/

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CropBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.CROPBLOCK, CropBlockEntity::tick);
    }

    @Nullable
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> blockEntityType, BlockEntityType<E> blockEntityType2, BlockEntityTicker<? super E> blockEntityTicker) {
        return blockEntityType2 == blockEntityType ? (BlockEntityTicker<A>) blockEntityTicker : null;
    }


    @Override
    public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, boolean bl) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level level, Random random, BlockPos blockPos, BlockState blockState) {
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, Random random, BlockPos blockPos, BlockState blockState) {

    }
}
