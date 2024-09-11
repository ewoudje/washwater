package com.wetwater.washwater.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

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
                System.out.println("Placed Water with Bucket");
                BucketMechanics.precisionBucketPlace(level, targetPos, itemStack, player);
            }
            else {
                System.out.println("Picked up Water with Bucket");
                BucketMechanics.precisionBucketPickup(level, targetPos, itemStack, player);
            }
        }
        return InteractionResult.PASS;
    }
}
