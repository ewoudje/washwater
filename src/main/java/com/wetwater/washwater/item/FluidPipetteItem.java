package com.wetwater.washwater.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class FluidPipetteItem extends Item {


    public FluidPipetteItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        Player player = useOnContext.getPlayer();
        ItemStack itemStack = useOnContext.getItemInHand();
        BlockPos targetPos = useOnContext.getClickedPos();
        if (true) {
            if (!player.isCrouching()) {
                System.out.println("Placed Water with Pipette");
                BucketMechanics.creativePipettePlace(level, targetPos, itemStack, player);
            }
            else {
                System.out.println("Picked up Water with Pipette");
                //BucketMechanics.creativePipettePickup(level, targetPos, itemStack, player);
                BucketMechanics.creativePipetteDebug(level, targetPos, itemStack, player);
            }

        }
        else {
        }
        return InteractionResult.PASS;
    }

/*    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide) {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            System.out.println("Did a thing!");
            return InteractionResultHolder.consume(itemStack);
        }
        else {
            return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
        }
    }*/
}
