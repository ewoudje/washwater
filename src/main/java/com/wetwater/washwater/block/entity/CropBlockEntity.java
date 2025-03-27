package com.wetwater.washwater.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CropBlockEntity extends BlockEntity {

    public CropBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CROPBLOCK, blockPos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CropBlockEntity entity) {
        //DO TICK STUFF
        if (!level.isClientSide) {
            System.out.println("amongala");
/*            if (FluidTicker.shouldTick((ServerLevel) level)) {
                performSpoutAction(level, pos);
            }*/
        }
    }

}
