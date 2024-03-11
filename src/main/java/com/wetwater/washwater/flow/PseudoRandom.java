package com.wetwater.washwater.flow;

import com.wetwater.washwater.WaterMod;
import net.minecraft.core.Direction;

public class PseudoRandom {
    private static final Direction[][] randomisedDirectionArrayArray = new Direction[4][4];

    static {
        for (int i = 0; i < 4; i++) {
            int x = i;
            for(Direction dir : Direction.Plane.HORIZONTAL) {
                randomisedDirectionArrayArray[i][x%4] = dir;
                x++;
            }
        }
    }

    public static Direction[] getRandomDirectionArray() {
        return randomisedDirectionArrayArray[(int) (WaterMod.currentTick % 4)];
    }
}
