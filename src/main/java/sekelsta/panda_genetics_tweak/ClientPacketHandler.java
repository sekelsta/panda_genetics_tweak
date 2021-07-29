package sekelsta.panda_genetics_tweak;

import java.util.function.Supplier;
import java.util.Optional;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fmllegacy.LogicalSidedProvider;
import net.minecraftforge.fmllegacy.network.NetworkEvent.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientPacketHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    // The SGenesPacket is sent from the server to the client, so this code
    // should only execute client-side.
    public static void handleGenesPacket(SGenesPacket packet, Supplier<Context> context) {
        Optional<ClientLevel> clientLevel = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT);
        if (!clientLevel.isPresent()) {
            LOGGER.warn("Could not get ClientWorld for SGenesPacket.");
            context.get().setPacketHandled(true);
            return;
        }

        // Enqueue anything that needs to be thread-safe
        context.get().enqueueWork(() -> {
            Entity entity = clientLevel.get().getEntity(packet.getEntityId());
            Util.setGeneticsTag(entity, packet.getGenes());
        });
        context.get().setPacketHandled(true);
    }
}
