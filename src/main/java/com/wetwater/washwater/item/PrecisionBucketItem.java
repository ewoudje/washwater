package com.wetwater.washwater.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
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
                BucketMechanics.precisionBucketPlace(level, targetPos, itemStack, player);
            }
            else {
                BucketMechanics.precisionBucketPickup(level, targetPos, itemStack, player);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        if (itemStack.hasTag()) {
            int bucketFillLevel = itemStack.getTag().getInt("washwater:bucketFillLevel");
            String toolTipText = "Bucket contains: " + bucketFillLevel + "l " + "of fluid";
            list.add(new TextComponent(toolTipText));
        }
    }
}
