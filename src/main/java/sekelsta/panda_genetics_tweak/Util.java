package sekelsta.panda_genetics_tweak;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class Util {
    // Helper for looking at gene data
    public static CompoundTag getGeneticsTag(Entity entity) {
        CompoundTag tag = entity.getPersistentData().getCompound("PandaGenetics");
        if (!entity.getPersistentData().contains("PandaGenetics")) {
            entity.getPersistentData().put("PandaGenetics", tag);
        }
        return tag;
    }

    public static void setGeneticsTag(Entity entity, CompoundTag tag) {
        entity.getPersistentData().put("PandaGenetics", tag);
    }
}
