package com.wetwater.washwater.item;

import com.wetwater.washwater.FluidManager;
import com.wetwater.washwater.WaterInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrecisionBucketItem extends Item {

    public PrecisionBucketItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        Player player = useOnContext.getPlayer();
        ItemStack itemStack = useOnContext.getItemInHand();
        BlockPos targetPos = useOnContext.getClickedPos();

        if (!itemStack.hasTag()) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("washwater:bucketFillLevel", 0);
            itemStack.setTag(tag);
        }
        if (player != null) {
            if (!player.isCrouching()) {
                precisionBucketPlace(level, targetPos, itemStack, player);
            }
            else {
                precisionBucketPickup(level, targetPos, itemStack, player);
            }
        }
        return InteractionResult.PASS;
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

/*    public static boolean bucketPlaceIncremental(Level level, BlockPos pos, ItemStack itemStack) {
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
    }*/


/*    public static void extractWater(ItemStack stack) {
        int previousWater = NbtUtil.getBucketNbtData(stack);
        NbtUtil.setBucketNbtData(stack, previousWater-1);
    }*/

/*    protected static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluid) {
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
    }*/

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        if (itemStack.hasTag()) {
            int bucketFillLevel = itemStack.getTag().getInt("washwater:bucketFillLevel");
            String toolTipText = "Bucket contains: " + bucketFillLevel + "l " + "of fluid";
            list.add(new TextComponent(toolTipText));
        }
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return Mth.color(56, 141, 252);
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            int fillLevel = itemStack.getTag().getInt("washwater:bucketFillLevel");
            int maxFillLevel = WaterInfo.volumePerBlock;
            float fraction = (float) fillLevel / (float) maxFillLevel;
            return (int) (13f * fraction);
        }
        else return 0;
    }
}
