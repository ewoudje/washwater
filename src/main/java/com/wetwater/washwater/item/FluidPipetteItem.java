package com.wetwater.washwater.item;

import com.wetwater.washwater.FluidManager;
import com.wetwater.washwater.WaterInfo;
import com.wetwater.washwater.WaterMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.BlockHitResult;

public class FluidPipetteItem extends Item {


    public FluidPipetteItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        Player player = useOnContext.getPlayer();
        ItemStack itemStack = useOnContext.getItemInHand();
        BlockPos targetPos = useOnContext.getClickedPos();
        if (!player.isCrouching()) {
            creativePipettePlace(level, targetPos, itemStack, player);
        }
        else {
            creativePipetteDebug(level, targetPos, itemStack, player);
            creativePipettePickup(level, targetPos, itemStack, player);
        }

        return InteractionResult.PASS;
    }

    public static boolean creativePipettePlace(Level level, BlockPos pos, ItemStack itemStack, Player player) {
        if (!level.isClientSide && pos.getY() != WaterInfo.minY) {
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.NONE);
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            FluidManager.addVolume((ServerLevel) level, blockPos2, 1);
        }
        return true;
    }

    public static boolean creativePipettePickup(Level level, BlockPos pos, ItemStack itemStack, Player player) {
        if (!level.isClientSide) {
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.NONE);
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            int oldVolume = FluidManager.getVolume(level, blockPos2);
            int newVolume = (oldVolume < 1) ? oldVolume : oldVolume - 1;
            FluidManager.setVolume((ServerLevel) level, blockPos2,  newVolume);
        }
        return true;
    }

    public static boolean creativePipetteDebug(Level level, BlockPos pos, ItemStack itemStack, Player player) {
        if (!level.isClientSide && pos.getY() != WaterInfo.minY) {
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.NONE);
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            //System.out.println("serverside, block at pos: " + level.getBlockState(blockPos2).getBlock());
            System.out.println("serverside, block at pos: " + level.getBlockState(blockPos2) + "vol: " + FluidManager.getVolume(level, blockPos2));

        }
        else {
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.NONE);
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            LevelAccessor levelAccessor;
            //System.out.println("clientside, block at pos: " + level.getBlockState(blockPos2).getBlock());
            System.out.println("clientside, block at pos: " + level.getBlockState(blockPos2) + "vol: " + FluidManager.getVolume(level, blockPos2));
        }
        return true;
    }

}
