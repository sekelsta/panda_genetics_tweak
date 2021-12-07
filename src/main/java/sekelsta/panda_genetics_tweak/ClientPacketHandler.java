package sekelsta.panda_genetics_tweak;

import java.util.function.Supplier;
import java.util.Optional;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientPacketHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    // The SGenesPacket is sent from the server to the client, so this code
    // should only execute client-side.
    public static void handleGenesPacket(SGenesPacket packet, Supplier<Context> context) {
        Optional<Level> clientLevel = LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT);
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
