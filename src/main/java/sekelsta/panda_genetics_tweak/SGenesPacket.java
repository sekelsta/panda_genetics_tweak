package sekelsta.panda_genetics_tweak;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SGenesPacket {
    private int entityId;
    private CompoundNBT genes;

    public SGenesPacket(int entityId, CompoundNBT genes) {
        this.entityId = entityId;
        this.genes = genes;
    }

    public SGenesPacket(Entity entity) {
        this(entity.getEntityId(), Util.getGeneticsTag(entity));
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeVarInt(this.entityId);
        buffer.writeCompoundTag(genes);
    }

    public static SGenesPacket decode(PacketBuffer buffer) {
        int id = buffer.readVarInt();
        CompoundNBT genes = buffer.readCompoundTag();
        return new SGenesPacket(id, genes);
    }

    public void handle(Supplier<Context> context) {
        // Enqueue anything that needs to be thread-safe
        context.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            Entity entity = world.getEntityByID(this.entityId);
            Util.setGeneticsTag(entity, this.genes);
        });
        context.get().setPacketHandled(true);
    }

}
