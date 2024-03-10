package com.wetwater.washwater.util;

import net.minecraft.core.Direction;

public class OnInit {

    public static void initializeThings() {
        initialiseRandomDirectionArray();
    }

    public static Direction[][] randomisedDirectionArrayArray = new Direction[4][4];

    public static void initialiseRandomDirectionArray() {
        for (int i = 0; i < 4; i++) {
            int x = i;
            for(Direction dir : Direction.Plane.HORIZONTAL) {
                randomisedDirectionArrayArray[i][x%4] = dir;
                x++;
            }
        }
    }

}
