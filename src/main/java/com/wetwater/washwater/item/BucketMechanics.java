package com.wetwater.washwater.item;

import com.wetwater.washwater.FluidManager;
import com.wetwater.washwater.WaterInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BucketMechanics {

    public static boolean bucketPlaceIncremental(Level level, BlockPos pos, ItemStack itemStack) {
        if (!level.isClientSide) {
            if (NbtUtil.getBucketNbtData(itemStack) > 0){
                extractWater(itemStack);
                FluidManager.addVolume((ServerLevel) level, pos, 1);
                return true;
            }
            else {
                return false;
            }
        } else {
            return false;
        }
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

    public static boolean precisionBucketPlace(Level level, BlockPos pos, ItemStack itemStack, Player player) {

        int bucketFillLevel = itemStack.getTag().getInt("washwater:bucketFillLevel");
        int newBucketFillLevel = 0;

        if (bucketFillLevel > 0 && !level.isClientSide && pos.getY() != WaterInfo.minY) {
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.NONE);
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            FluidManager.addVolume((ServerLevel) level, blockPos2, bucketFillLevel);
            CompoundTag tag = new CompoundTag();
            tag.putInt("washwater:bucketFillLevel", newBucketFillLevel);
            itemStack.setTag(tag);
        }
        return true;
    }
    public static boolean precisionBucketPickup(Level level, BlockPos pos, ItemStack itemStack, Player player) {
        int bucketFillLevel = itemStack.getTag().getInt("washwater:bucketFillLevel");
        int bucketRemainingSpace = WaterInfo.volumePerBlock - bucketFillLevel;
        if (!level.isClientSide && pos.getY() != WaterInfo.minY) {
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.NONE);
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            int oldVolume = FluidManager.getVolume(level, blockPos2);
            int newVolume = 0;
            int newBucketFillLevel;
            if (oldVolume > bucketRemainingSpace) {
                newVolume = oldVolume - bucketRemainingSpace;
                newBucketFillLevel = WaterInfo.volumePerBlock;
            }
            else {
                newBucketFillLevel = bucketFillLevel + oldVolume;
            }
            FluidManager.setVolume((ServerLevel) level, blockPos2,  newVolume);
            CompoundTag tag = new CompoundTag();
            tag.putInt("washwater:bucketFillLevel", newBucketFillLevel);
            itemStack.setTag(tag);
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


    protected static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluid) {
        float f = player.getXRot();
        float g = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float h = Mth.cos(-g * 0.017453292F - 3.1415927F);
        float i = Mth.sin(-g * 0.017453292F - 3.1415927F);
        float j = -Mth.cos(-f * 0.017453292F);
        float k = Mth.sin(-f * 0.017453292F);
        float l = i * j;
        float n = h * j;
        double d = 5.0;
        Vec3 vec32 = vec3.add((double)l * 5.0, (double)k * 5.0, (double)n * 5.0);
        return level.clip(new ClipContext(vec3, vec32, net.minecraft.world.level.ClipContext.Block.OUTLINE, fluid, player));
    }

    public static void extractWater(ItemStack stack) {
        int previousWater = NbtUtil.getBucketNbtData(stack);
        NbtUtil.setBucketNbtData(stack, previousWater-1);
    }

}
