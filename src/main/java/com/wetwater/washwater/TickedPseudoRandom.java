package com.wetwater.washwater;

import com.wetwater.washwater.util.OnInit;
import net.minecraft.core.Direction;

public class TickedPseudoRandom {

    public static int a = 0;

    //public static Direction[][] randomisedDirectionArrayArray = new Direction[4][4];


    public static void increaseTickCounter() {
        a++;
    }

    //TODO move to non-ticked class
/*    public static void initialiseRandomDirectionArray() {
        for (int i = 0; i < 4; i++) {
            int x = i;
            for(Direction dir : Direction.Plane.HORIZONTAL) {
                randomisedDirectionArrayArray[i][x%4] = dir;
                x++;
            }
        }
    }*/

    public static Direction[] getRandomDirectionArray() {
        return OnInit.randomisedDirectionArrayArray[a%4];
    }
}
