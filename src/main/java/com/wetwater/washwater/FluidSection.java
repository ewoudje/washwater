package com.wetwater.washwater;

import com.ewoudje.lasagna.chunkstorage.ExtraSectionStorage;
import com.ewoudje.lasagna.chunkstorage.ExtraStorageSectionContainer;
import com.ewoudje.lasagna.networking.LasagnaNetworking;
import com.ewoudje.lasagna.networking.TrackingChunkPacketTarget;
import com.wetwater.washwater.packets.DeltaFluidSectionPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.jetbrains.annotations.NotNull;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO more fluid supports
public class FluidSection implements ExtraSectionStorage {
    public static final ResourceLocation ID = WaterMod.resource("water");
    public static final List<FluidSection> dirtySections = new ArrayList<>();
    private final short[] water = new short[16*16*16];
    private int[] dirty = null;
    private int dirtyCount = 0;
    private boolean anyDirt = false;
    private boolean isSavedDirty = false;
    private final LevelChunk chunk;
    private final int sectionIndex;

    public FluidSection(LevelChunk chunk, int sectionIndex) {
        this.chunk = chunk;
        this.sectionIndex = sectionIndex;

        Arrays.fill(water, Short.MIN_VALUE);

        if (!chunk.getLevel().isClientSide()) {
            dirty = new int[128]; // 16*16*16 / 32 = 128
            dirtyCount = 16*16*16;
            Arrays.fill(dirty, 0xFFFFFFFF);
        }

        var section = chunk.getSections()[sectionIndex];
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    if (!section.getFluidState(x, y, z).isEmpty()) {
                        setWaterVolume(x, y, z, WaterInfo.volumePerBlock);
                    } else {
                        setWaterVolume(x, y, z, (short) (section.getBlockState(x, y, z).isAir() ? 0 : -1));
                    }
                }
            }
        }

        isSavedDirty = true;
    }

    private FluidSection(ShortBuffer shorts, @NotNull LevelChunk chunk, int sectionIndex) {
        shorts.get(water);

        this.chunk = chunk;
        this.sectionIndex = sectionIndex;

        if (!chunk.getLevel().isClientSide()) {
            dirty = new int[128]; // 16*16*16 / 32 = 128
            dirtyCount = 16*16*16;
            Arrays.fill(dirty, 0xFFFFFFFF);
        }
    }

    private byte[] makeBuffer() {
        byte[] buffer = new byte[water.length * 2];
        for (int i = 0; i < water.length; i++) {
            buffer[i * 2] = (byte) (water[i] & 0xFF);
            buffer[i * 2 + 1] = (byte) ((water[i] >> 8) & 0xFF);
        }

        return buffer;
    }

    @NotNull
    @Override
    public CompoundTag writeNBT(@NotNull CompoundTag nbtCompound, @NotNull LevelChunk chunk, int sectionIndex) {
        nbtCompound.putByteArray("water", makeBuffer());
        return nbtCompound;
    }

    @Override
    public FriendlyByteBuf writePacket(@NotNull FriendlyByteBuf byteBuff, @NotNull LevelChunk chunk, int sectionIndex) {
        byteBuff.writeByteArray(makeBuffer());
        return byteBuff;
    }

    public static FluidSection read(CompoundTag nbt, @NotNull LevelChunk chunk, int sectionIndex) {
        var bytes = nbt.getByteArray("water");
        return new FluidSection(ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer(), chunk, sectionIndex);
    }

    public static FluidSection readPacket(FriendlyByteBuf buf, @NotNull LevelChunk chunk, int sectionIndex) {
        return new FluidSection(ByteBuffer.wrap(buf.readByteArray()).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer(), chunk, sectionIndex);
    }

    public short getWaterVolume(BlockPos pos) {
        int x = pos.getX() & 15;
        int y = pos.getY() & 15;
        int z = pos.getZ() & 15;

        return getWaterVolume(x, y, z);
    }

    public short getWaterVolume(int x, int y, int z) {
        return water[(x*16*16) + (y * 16) + z];
    }

    public void setWaterVolume(BlockPos pos, short value) {
        setWaterVolume(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, value);
    }

    // relative coordinates
    public void setWaterVolume(int x, int y, int z, short value) {
        water[(x*16*16) + (y * 16) + z] = value;
        isSavedDirty = true;

        if (dirty != null) {
            dirty[x * 8 + y / 2] |= 1 << (z + ((y % 4) * 16));
            dirtyCount++;
            if (!anyDirt) {
                anyDirt = true;
                dirtySections.add(this);
            }
        }
    }


    public void setWaterVolumeByState(BlockPos pos, BlockState state) {
        setWaterVolumeByState(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, state);
    }

    // relative coordinates
    public void setWaterVolumeByState(int x, int y, int z, BlockState state) {
        setWaterVolume(x, y, z, WaterInfo.getWaterVolumeOfState(state));
    }

    private DeltaFluidSectionPacket makeDelta() {
        // Happens if 2 times in the list or incorrectly added to the list?
        if (!anyDirt) throw new IllegalStateException("No dirty section expected to make delta packet");
        if (dirtyCount == 0) throw new IllegalStateException("No dirtyCount section expected to make delta packet");

        DeltaFluidSectionPacket delta = new DeltaFluidSectionPacket(
                this.chunk.getPos().x,
                this.chunk.getPos().z,
                this.sectionIndex,
                this.dirty, this.water, this.dirtyCount
        );

        anyDirt = false;
        dirtyCount = 0;
        Arrays.fill(dirty, 0);

        return delta;
    }

    public static void sendUpdates() {
        for (var section : dirtySections) {
            LasagnaNetworking.send(
                    new TrackingChunkPacketTarget(section.chunk),
                    DeltaFluidSectionPacket.class,
                    section.makeDelta()
            );
        }

        dirtySections.clear();
    }

    public void applyDelta(DeltaFluidSectionPacket packet) {
        if (packet.positions == null) {
            ByteBuffer.wrap(packet.water).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(this.water);
        } else {
            for (int i = 0; i < (packet.positions.length / 2); i++) {
                if (packet.positions[i * 2] == -1 && packet.positions[i * 2 + 1] == -1) break;
                int position = (packet.positions[i * 2] & 0xFF) | ((packet.positions[(i * 2) + 1] & 0xFF) << 8);
                this.water[position] = (short) ((packet.water[i * 2] & 0xFF) | ((packet.water[(i * 2) + 1] & 0xFF) << 8));
            }
        }
    }

    public static void register() {
        DeltaFluidSectionPacket.register();
        ExtraSectionStorage.Companion.register(FluidSection.ID, FluidSection::read, true, FluidSection.class, FluidSection::readPacket);
    }

    public static FluidSection getOrMake(LevelChunk chunk, int sectionIndex) {
        var section = chunk.getSections()[sectionIndex];
        FluidSection fSection = (FluidSection) ((ExtraStorageSectionContainer) section).getSectionStorage(FluidSection.ID);

        if (fSection == null) {
            fSection = new FluidSection(chunk, sectionIndex);
            ((ExtraStorageSectionContainer) section).setSectionStorage(FluidSection.ID, fSection);
        }

        return fSection;
    }

    @Override // Dirty for saving
    public boolean isDirty() {
        return isSavedDirty;
    }

    @Override
    public void saved() {
        isSavedDirty = false;
    }
}
