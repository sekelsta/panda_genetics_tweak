package sekelsta.panda_genetics_tweak;

import java.util.Locale;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PandaRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Panda;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BrownPandaRenderer extends PandaRenderer {
    final static String PATH = "textures/entity/panda/";
    final static String SUFFIX = "panda.png";

    public BrownPandaRenderer(EntityRendererProvider.Context renderer) {
        super(renderer);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(Panda panda) {
        boolean isBrown = Util.getGeneticsTag(panda).getBoolean("brown1") 
                        && Util.getGeneticsTag(panda).getBoolean("brown2");
        Panda.Gene type = panda.getVariant();
        String name = type.toString().toLowerCase(Locale.ENGLISH);
        if (type == Panda.Gene.BROWN || type == Panda.Gene.NORMAL) {
            name = "";
        }
        else {
            name += "_";
        }
        String prefix = "";
        if (isBrown) {
            prefix = "brown_";
        }
        String domain = PandaGenetics.MODID;
        if (!isBrown || name == "") {
            domain = "minecraft";
        }
        return new ResourceLocation(domain, PATH + prefix + name + SUFFIX);
    }
}
