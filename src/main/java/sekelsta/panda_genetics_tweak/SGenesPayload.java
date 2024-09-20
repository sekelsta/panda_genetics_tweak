package sekelsta.panda_genetics_tweak;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;
import java.util.Optional;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.LogicalSidedProvider;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class SGenesPayload implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(PandaGenetics.MODID, "sgenes");

    private int entityID;
    private CompoundTag genes;

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.entityID);
        buffer.writeNbt(genes);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public SGenesPayload(int entityID, CompoundTag genes) {
        this.entityID = entityID;
        this.genes = genes;
    }

    public SGenesPayload(Entity entity) {
        this(entity.getId(), Util.getGeneticsTag(entity));
    }

    public static SGenesPayload decode(FriendlyByteBuf buffer) {
        int id = buffer.readVarInt();
        CompoundTag genes = buffer.readNbt();
        return new SGenesPayload(id, genes);
    }

    // The SGenesPacket is sent from the server to the client, so this code should only execute client-side.
    // Note: After the 1.20.4 Neoforged networking rework, this is no longer guaranteed by the API but should still hold.
    public void handle(PlayPayloadContext context) {
        if (!context.level().isPresent()) {
            PandaGenetics.LOGGER.warn("Could not get ClientWorld for SGenesPacket.");
            return;
        }

        context.workHandler().execute(() -> {
            Entity entity = context.level().get().getEntity(entityID);
            Util.setGeneticsTag(entity, genes);
        });
    }
}
