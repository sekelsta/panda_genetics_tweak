package sekelsta.panda_genetics_tweak;

import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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
    public static void sendGenesPacket(ServerPlayer player, Entity entity) {
        SGenesPacket packet = new SGenesPacket(entity);
        CHANNEL.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
