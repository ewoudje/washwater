package com.wetwater.washwater.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public class CropUtil {

    public static float getCurrentInsolation(ServerLevel level, BlockPos pos) {
        return getTimeRelativeInsolation(level) * getBiomeInsolationCoefficient(level, pos);
    }

    public static float getBiomeInsolationCoefficient(ServerLevel level, BlockPos pos) {
        Biome biome = level.getBiome(pos).value();
        return (biome.getBaseTemperature()+2) / 4f;
    }

    public static float getBiomeTemperature(ServerLevel level, BlockPos pos) {
        Biome biome = level.getBiome(pos).value();
        return (biome.getBaseTemperature() - 0.2f) * 25;
    }

    public static float getDownfallCoefficient(ServerLevel level, BlockPos pos) {
        Biome biome = level.getBiome(pos).value();
        return biome.getDownfall();
    }

    public static boolean isGettingDirectSunlight(ServerLevel level, BlockPos pos) {
        int i = level.getBrightness(LightLayer.SKY, pos) - level.getSkyDarken();
        return i > 9;
    }

    public static float getSunLight(ServerLevel level, BlockPos pos) {
        int i = level.getBrightness(LightLayer.SKY, pos) - level.getSkyDarken();
        return (float) i;
    }

    public static float getTimeRelativeInsolation(ServerLevel level) {
        return (float) Mth.clamp(Math.sin(level.getSunAngle(1.0f) + (Math.PI/2)), 0, 1);
    }

    public static int getMainGrowthNutrient(BlockState state) {
        return 0;
    }

    //GROWTH METHODS



    // METHODS SPECIFIC TO THE TIME-BASED APPROACH



}
