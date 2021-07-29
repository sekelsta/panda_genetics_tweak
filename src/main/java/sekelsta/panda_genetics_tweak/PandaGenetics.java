package sekelsta.panda_genetics_tweak;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Panda;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.InterModComms;
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

    private void clientSetup(final EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(EntityType.PANDA, BrownPandaRenderer::new);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        PandaGeneticsPacketHandler.registerPackets();
    }

    private void loadPanda(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof Panda)) {
            return;
        }
        Panda panda = (Panda)event.getEntity();
        if (!Util.getGeneticsTag(panda).contains("brown1")) {
            Panda.Gene main = panda.getMainGene();
            Util.getGeneticsTag(panda).putBoolean("brown1", main == Panda.Gene.BROWN);
        }
        if (!Util.getGeneticsTag(panda).contains("brown2")) {
            Panda.Gene hidden = panda.getHiddenGene();
            Util.getGeneticsTag(panda).putBoolean("brown2", hidden == Panda.Gene.BROWN);
        }
    }

    private void breedPanda(BabyEntitySpawnEvent event) {
        if (!(event.getChild() instanceof Panda)) {
            return;
        }
        Mob mother = event.getParentA();
        Mob father = event.getParentB();
        AgeableMob child = event.getChild();
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
        if (event.getTarget() instanceof Panda
                && event.getPlayer() instanceof ServerPlayer) {
            PandaGeneticsPacketHandler.sendGenesPacket((ServerPlayer)event.getPlayer(), event.getTarget());
        }
    }
}
