package sekelsta.panda_genetics_tweak;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;

public class Util {
    // Helper for looking at gene data
    public static CompoundNBT getGeneticsTag(Entity entity) {
        CompoundNBT tag = entity.getPersistentData().getCompound("PandaGenetics");
        if (!entity.getPersistentData().contains("PandaGenetics")) {
            entity.getPersistentData().put("PandaGenetics", tag);
        }
        return tag;
    }

    public static void setGeneticsTag(Entity entity, CompoundNBT tag) {
        entity.getPersistentData().put("PandaGenetics", tag);
    }
}
