package sekelsta.panda_genetics_tweak;

import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class SGenesPacket {
    private int entityId;
    private CompoundTag genes;

    public int getEntityId() {
        return entityId;
    }

    public CompoundTag getGenes() {
        return genes;
    }

    public SGenesPacket(int entityId, CompoundTag genes) {
        this.entityId = entityId;
        this.genes = genes;
    }

    public SGenesPacket(Entity entity) {
        this(entity.getId(), Util.getGeneticsTag(entity));
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.entityId);
        buffer.writeNbt(genes);
    }

    public static SGenesPacket decode(FriendlyByteBuf buffer) {
        int id = buffer.readVarInt();
        CompoundTag genes = buffer.readNbt();
        return new SGenesPacket(id, genes);
    }
}
