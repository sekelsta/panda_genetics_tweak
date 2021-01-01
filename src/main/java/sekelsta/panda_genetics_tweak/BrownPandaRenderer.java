package sekelsta.panda_genetics_tweak;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PandaRenderer;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;

@OnlyIn(Dist.CLIENT)
public class BrownPandaRenderer extends PandaRenderer {
    final static String PATH = "textures/entity/panda/";
    final static String SUFFIX = "panda.png";

    public BrownPandaRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getEntityTexture(PandaEntity panda) {
        boolean isBrown = Util.getGeneticsTag(panda).getBoolean("brown1") 
                        && Util.getGeneticsTag(panda).getBoolean("brown2");
        PandaEntity.Gene type = panda.func_213590_ei();
        String name = type.getName();
        if (type == PandaEntity.Gene.BROWN || type == PandaEntity.Gene.NORMAL) {
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
