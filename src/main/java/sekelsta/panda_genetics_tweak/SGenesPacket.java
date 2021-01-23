package sekelsta.panda_genetics_tweak;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class SGenesPacket {
    private int entityId;
    private CompoundNBT genes;

    public int getEntityId() {
        return entityId;
    }

    public CompoundNBT getGenes() {
        return genes;
    }

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
}
