package com.wetwater.washwater;

import com.ewoudje.lasagna.chunkstorage.ExtraStorageSectionContainer;
import com.ewoudje.lasagna.networking.LasagnaNetworking;
import com.wetwater.washwater.mixin.client.LevelRendererAccessor;
import com.wetwater.washwater.packets.DeltaFluidSectionPacket;
import kotlin.Unit;

public class ClientNetworking {

    public static void register() {
        LasagnaNetworking.packetClient(DeltaFluidSectionPacket.class, false, (packet, context) -> {
            var chunk = context.level.getChunk(
                    packet.chunkX,
                    packet.chunkZ
            );
            var section = chunk.getSections()[packet.sectionY];

            FluidSection storage = (FluidSection) ((ExtraStorageSectionContainer) section).getSectionStorage(FluidSection.ID);

            if (storage == null) {
                storage = new FluidSection(chunk, packet.sectionY);
                ((ExtraStorageSectionContainer) section).setSectionStorage(FluidSection.ID, storage);
            }

            storage.applyDelta(packet);

            ((LevelRendererAccessor) context.levelRenderer).iLoveItDirty(
                    packet.chunkX,
                    context.level.getSectionYFromSectionIndex(packet.sectionY),
                    packet.chunkZ,
                    true
            );
            return Unit.INSTANCE;
        });
    }

}
