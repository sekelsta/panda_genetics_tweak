package sekelsta.panda_genetics_tweak;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Panda;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;

import java.util.Random;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PandaGenetics.MODID)
public class PandaGenetics
{
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "panda_genetics_tweak";

    public static Random rand = new Random();

    public PandaGenetics(IEventBus modEventBus) {
        modEventBus.addListener(this::clientSetup);
        NeoForge.EVENT_BUS.addListener(this::loadPanda);
        NeoForge.EVENT_BUS.addListener(this::breedPanda);
        NeoForge.EVENT_BUS.addListener(this::trackPanda);
        modEventBus.addListener(PandaGeneticsNetworking::register);
    }

    private void clientSetup(final EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(EntityType.PANDA, BrownPandaRenderer::new);
    }

    private void loadPanda(EntityJoinLevelEvent event) {
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
                && event.getEntity() instanceof ServerPlayer) {
            PandaGeneticsNetworking.sendGenesPacket((ServerPlayer)event.getEntity(), event.getTarget());
        }
    }
}
