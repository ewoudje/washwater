package com.wetwater.washwater.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NbtUtil {


    public static void setBucketNbtData(ItemStack bucketStack, int waterVolume) {
        CompoundTag nbtData = new CompoundTag();
        nbtData.putInt("washwater.bucketStoredWater", waterVolume);
        bucketStack.setTag(nbtData);
    }

    public static int getBucketNbtData(ItemStack bucketStack) {
        return (bucketStack.getTag() != null) ? bucketStack.getTag().getInt("washwater.bucketStoredWater") : 0;
    }

}
