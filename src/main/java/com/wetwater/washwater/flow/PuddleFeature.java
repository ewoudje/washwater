package com.wetwater.washwater.flow;

import com.wetwater.washwater.WaterInfo;
import com.wetwater.washwater.util.PathfinderBFS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class PuddleFeature {

    static Boolean PUDDLE_FEATURE_ENABLED = true;

    public static final int PUDDLE_RADIUS = 4;
    public static final int PUDDLE_DIAMETER = PUDDLE_RADIUS * 2 + 1;
    private static BlockPos pos;
    private static int bfsMatrix[][] = new int[PUDDLE_DIAMETER][PUDDLE_DIAMETER];
    private static List<PathfinderBFS.Node> holes = new ArrayList<>(8);
    private static int xX;
    private static int zZ;

    public static void execute(BlockPos center, int volume, FluidRegion region) {
        if (!PUDDLE_FEATURE_ENABLED) return;
        System.out.println("puddle called");
        //setWaterLevel(volume, center, world);
        pos = center;

        if (!isNotFull(pos.below(), region)) {
            System.out.println("aAA");
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            // fill in the bfsMatrix
            xX = x - PUDDLE_RADIUS;
            zZ = z - PUDDLE_RADIUS;

            for (int iX = 0; iX < PUDDLE_DIAMETER; iX++) {
                for (int iZ = 0; iZ < PUDDLE_DIAMETER; iZ++) {
                    BlockPos internalPos = new BlockPos(iX + xX, y, iZ + zZ);
                    bfsMatrix[iX][iZ] = region.getVolume(internalPos) == 0 ? 9 : -1;
                }
            }



            //union start
            for (int currentRadius = 1; currentRadius <= PUDDLE_RADIUS; currentRadius++) {
                int xL = x - currentRadius;
                int xR = x + currentRadius;
                int zT = z + currentRadius;
                int zB = z - currentRadius;

                holes.clear();

                testLine(xL, zT, xR, zT, region);
                testLine(xL, zB, xR, zB, region);
                testLine(xL, zB, xL, zT, region);
                testLine(xR, zB, xR, zT, region);

                if (holes.isEmpty())
                    continue;

                bfsMatrix[4][4] = -3;

                if (holeFound(holes, volume, region))
                    break;
            }
        }
        else {
            //Flow down
            var underVolume = region.getVolume(pos.getX(), pos.getY() - 1 , pos.getZ());
            var transaction = Math.min(volume, WaterInfo.volumePerBlock - underVolume);
            region.setVolume(pos, volume - transaction);
            region.setVolume(pos.getX(), pos.getY() - 1, pos.getZ(), underVolume + transaction);
        }
    }

    private static boolean holeFound(List<PathfinderBFS.Node> holes, int volume, FluidRegion region) {
        int[][] result = PathfinderBFS.distanceMapperBFS(bfsMatrix, holes);

        int minDistance = 255;
        Direction direction = null;

        if (result[4][3] < minDistance && result[4][3] >= 0) {
            minDistance = result[4][3];
            direction = Direction.NORTH;
        }
        if (result[3][4] < minDistance && result[3][4] >= 0) {
            minDistance = result[3][4];
            direction = Direction.WEST;
        }
        if (result[4][5] < minDistance && result[4][5] >= 0) {
            minDistance = result[4][5];
            direction = Direction.SOUTH;
        }
        if (result[5][4] < minDistance && result[5][4] >= 0) {
            minDistance = result[5][4];
            direction = Direction.EAST;
        }
        System.out.println("direction null: " + direction == null);
        if (minDistance <= 4 && direction != null) {
            move(direction, volume, region);
            return true;
        }
        return false;
    }

    private static boolean isNotFull(BlockPos pos, FluidRegion region) {
        System.out.println(region.getVolume(pos));
        return region.getVolume(pos) < WaterInfo.volumePerBlock && region.getVolume(pos) > -1;
    }

    private static void move(Direction direction, int volume, FluidRegion region) {
        System.out.println("moved");
        region.setVolume(pos, 0);
        region.setVolume(pos.offset(direction.getNormal()), region.getVolume(pos.offset(direction.getNormal())) + volume);
    }

    // its actual test rect but ssssh...
    private static void testLine(int x, int z, int toX, int toZ, FluidRegion region) {
        BlockPos testPos;

        for (int iX = x; iX <= toX; iX++) {
            for (int iZ = z; iZ <= toZ; iZ++) {
                int relX = iX-xX;
                int relZ = iZ-zZ;
                testPos = new BlockPos(iX, pos.getY() - 1, iZ);
                int uLevel = region.getVolume(iX, pos.getY(), iZ);
                if (isNotFull(testPos, region) && (uLevel > -1 && uLevel < WaterInfo.surfaceTensionLimit)) {
                    holes.add(new PathfinderBFS.Node(relX, relZ, 0));
                }
            }
        }
    }

    private static void printMatrix(int[][] matrix) {
        // print result of bfs
        for (int a = matrix.length - 1; a >= 0; a--) {
            for (int b = matrix.length - 1; b >= 0; b--) {
                System.out.print((matrix[b][a] < 0 ? "" : " ") + matrix[b][a] + " ");
            }
            System.out.println();
        }
    }

}