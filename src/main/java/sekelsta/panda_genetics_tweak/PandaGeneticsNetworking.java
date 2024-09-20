package sekelsta.panda_genetics_tweak;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class PandaGeneticsNetworking {
    private static final String PROTOCOL_VERSION = "1";

    public static void register(final RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(PandaGenetics.MODID)
                .versioned(PROTOCOL_VERSION);
        registrar.play(SGenesPayload.ID, SGenesPayload::decode, SGenesPayload::handle);
    }

    // Send entity's gene info to player
    public static void sendGenesPacket(ServerPlayer player, Entity entity) {
        SGenesPayload packet = new SGenesPayload(entity);
        PacketDistributor.PLAYER.with(player).send(packet);
    }
}

