package com.wetwater.washwater.block.custom;

import com.wetwater.washwater.block.entity.CosmicMonoxideSpoutBlockEntity;
import com.wetwater.washwater.block.entity.FathomlessFluidChasmBlockEntity;
import com.wetwater.washwater.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FathomlessFluidChasmBlock extends BaseEntityBlock implements EntityBlock {

    public FathomlessFluidChasmBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FathomlessFluidChasmBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.FATHOMLESS_FLUID_CHASM, FathomlessFluidChasmBlockEntity::tick);
    }
}
