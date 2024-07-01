package com.wetwater.washwater.flow;

import com.wetwater.washwater.FluidSection;
import it.unimi.dsi.fastutil.longs.LongConsumer;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class SectionCenteredFluidRegion implements FluidRegion {
    private final static int FIFTEEN_MASK = ~15;
    private final int xCenter, yCenter, zCenter;
    private final FluidSection center;
    private final FluidSection[] around;
    private final LongConsumer onUpdate;


    public SectionCenteredFluidRegion(int xCenter, int yCenter, int zCenter, FluidSection center, FluidSection[] around, LongConsumer onUpdate) {
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.zCenter = zCenter;
        this.center = center;
        this.around = around;
        this.onUpdate = onUpdate;
    }

    @Override
    public int getVolume(int x, int y, int z) {
        return getSection(x - xCenter, y - yCenter, z - zCenter)
                .getWaterVolume(x & 15, y & 15, z & 15);
    }

    @Override
    public void setVolume(int x, int y, int z, int volume) {
        getSection(x - xCenter, y - yCenter, z - zCenter)
                .setWaterVolume(x & 15, y & 15, z & 15, (short) volume);

        onUpdate.accept(BlockPos.asLong(x, y, z));
        for (Direction direction : Direction.values()) {
            onUpdate.accept(
                    BlockPos.asLong(x + direction.getStepX(), y + direction.getStepY(), z + direction.getStepZ())
            );
        }
    }

    private FluidSection getSection(int rX, int rY, int rZ) {
        if (
                (rX & FIFTEEN_MASK) == 0 &&
                        (rY & FIFTEEN_MASK) == 0 &&
                        (rZ & FIFTEEN_MASK) == 0
        ) {
            return center;
        } else {
            //TODO
            throw new UnsupportedOperationException();
        }
    }
}
