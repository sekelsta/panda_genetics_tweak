package sekelsta.panda_genetics_tweak;

import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PandaGeneticsPacketHandler {
    private static int ID = 0;

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        // Name
        new ResourceLocation(PandaGenetics.MODID, "main"),
        // Protocol version supplier
        () -> PROTOCOL_VERSION,
        // Predicate - client compatible protocol versions
        PROTOCOL_VERSION::equals,
        // Predicate - server compatible protocol versions
        PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        CHANNEL.registerMessage(ID++, SGenesPacket.class, SGenesPacket::encode,
            SGenesPacket::decode, ClientPacketHandler::handleGenesPacket,
            Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    // Send entity's gene info to player
    public static void sendGenesPacket(ServerPlayerEntity player, Entity entity) {
        SGenesPacket packet = new SGenesPacket(entity);
        CHANNEL.sendTo(packet, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
