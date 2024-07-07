package com.wetwater.washwater.item;

import com.ewoudje.lasagna.chunkstorage.ExtraStorageSectionContainer;
import com.wetwater.washwater.FluidManager;
import com.wetwater.washwater.FluidSection;
import com.wetwater.washwater.FluidSectionContainer;
import com.wetwater.washwater.debug.DebugUtils;
import com.wetwater.washwater.mixin.MixinLevelChunkSection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
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
        if (!level.isClientSide) {
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

    public static boolean pipetteDebug(Level level, BlockPos pos, ItemStack itemStack, Player player) {
        if (!level.isClientSide) {
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, net.minecraft.world.level.ClipContext.Fluid.NONE);
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos blockPos2 = blockPos.relative(direction);
            System.out.println("blockpos is: " + blockPos2);
            LevelChunk chunk = level.getChunk(blockPos2.getX() >> 4, blockPos2.getZ() >> 4);
            LevelChunkSection section = chunk.getSections()[level.getSectionIndex(blockPos2.getY())];
            boolean hasOnlyAir = section.hasOnlyAir();
            System.out.println("Section empty: " + hasOnlyAir);
            boolean isNull = DebugUtils.debugMethodCheckNullSection((ServerLevel) level, blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
            System.out.println("Fsection null: " + isNull);
            //boolean isEmpty = DebugUtils.debugMethodCheckEmptySection((ServerLevel) level, blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
            //System.out.println("Fsection empty: " + isEmpty);
            //((FluidSectionContainer)section).setFluidSection(new FluidSection(chunk, level.getSectionIndex(blockPos2.getY())));
            FluidSection fluidSection2 = ((FluidSectionContainer)section).getFluidSection();
            System.out.println("Fsection NEW check: " + (fluidSection2 == null));
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
