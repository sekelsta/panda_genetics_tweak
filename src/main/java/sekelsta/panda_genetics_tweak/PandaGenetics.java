package sekelsta.panda_genetics_tweak;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PandaGenetics.MODID)
public class PandaGenetics
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "panda_genetics_tweak";

    public static Random rand = new Random();

    public PandaGenetics() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(this::loadPanda);
        MinecraftForge.EVENT_BUS.addListener(this::breedPanda);
        MinecraftForge.EVENT_BUS.addListener(this::trackPanda);
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityType.PANDA, BrownPandaRenderer::new);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        PandaGeneticsPacketHandler.registerPackets();
    }

    private void loadPanda(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof PandaEntity)) {
            return;
        }
        PandaEntity panda = (PandaEntity)event.getEntity();
        // Note the type is PandaEntity.Type in 1.15 mcp but PandaEntity.Gene in 1.16
        if (!Util.getGeneticsTag(panda).contains("brown1")) {
            PandaEntity.Gene main = panda.getMainGene();
            Util.getGeneticsTag(panda).putBoolean("brown1", main == PandaEntity.Gene.BROWN);
        }
        if (!Util.getGeneticsTag(panda).contains("brown2")) {
            PandaEntity.Gene hidden = panda.getHiddenGene();
            Util.getGeneticsTag(panda).putBoolean("brown2", hidden == PandaEntity.Gene.BROWN);
        }
    }

    private void breedPanda(BabyEntitySpawnEvent event) {
        if (!(event.getChild() instanceof PandaEntity)) {
            return;
        }
        MobEntity mother = event.getParentA();
        MobEntity father = event.getParentB();
        AgeableEntity child = event.getChild();
        // Inherit from parents
        boolean brown1 = Util.getGeneticsTag(mother).getBoolean("brown1");
        if (rand.nextBoolean()) {
            brown1 = Util.getGeneticsTag(mother).getBoolean("brown2");
        }
        boolean brown2 = Util.getGeneticsTag(father).getBoolean("brown1");
        if (rand.nextBoolean()) {
            brown2 = Util.getGeneticsTag(father).getBoolean("brown2");
        }
        // Mutate
        if (rand.nextInt(32) == 0) {
            brown1 = rand.nextInt(8) == 0;
        }
        if (rand.nextInt(32) == 0) {
            brown2 = rand.nextInt(8) == 0;
        }
        // Pass to child
        Util.getGeneticsTag(child).putBoolean("brown1", brown1);
        Util.getGeneticsTag(child).putBoolean("brown2", brown2);
    }

    private void trackPanda(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof PandaEntity
                && event.getPlayer() instanceof ServerPlayerEntity) {
            PandaGeneticsPacketHandler.sendGenesPacket((ServerPlayerEntity)event.getPlayer(), event.getTarget());
        }
    }
}
