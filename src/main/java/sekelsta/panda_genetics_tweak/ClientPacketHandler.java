package sekelsta.panda_genetics_tweak;

import java.util.function.Supplier;
import java.util.Optional;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientPacketHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    // The SGenesPacket is sent from the server to the client, so this code
    // should only execute client-side.
    public static void handleGenesPacket(SGenesPacket packet, Supplier<Context> context) {
        Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT);
        if (!clientWorld.isPresent()) {
            LOGGER.warn("Could not get ClientWorld for SGenesPacket.");
            context.get().setPacketHandled(true);
            return;
        }

        // Enqueue anything that needs to be thread-safe
        context.get().enqueueWork(() -> {
            Entity entity = clientWorld.get().getEntityByID(packet.getEntityId());
            Util.setGeneticsTag(entity, packet.getGenes());
        });
        context.get().setPacketHandled(true);
    }
}
