package dev.sleep.scorelib.common.network.packet;

import com.google.common.collect.HashBasedTable;
import dev.sleep.scorelib.common.network.NetworkManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;

public class PacketManager {

    private static final HashBasedTable<UUID, String, IPacket> STORED_PACKET_LIST = HashBasedTable.create();

    public static void sendPacketIfPossible(ServerPlayerEntity serverPlayer, PacketDistributor.PacketTarget packetDistributor, String packetName, IPacket packet) {
        IPacket oldPacket = STORED_PACKET_LIST.get(serverPlayer.getUUID(), packetName);

        if (oldPacket == null) {
            sendAndStore(serverPlayer, packetDistributor, packetName, packet);
            return;
        }

        if (oldPacket.equals(packet)) {
            return;
        }

        sendAndStore(serverPlayer, packetDistributor, packetName, packet);
    }

    public static void sendPacket(PacketDistributor.PacketTarget packetDistributor, IPacket packet) {
        NetworkManager.handleSplitting(packet, s -> NetworkManager.getNetworkChannel().send(packetDistributor, s));
    }

    public static void sendPacketToServer(IPacket packet) {
        NetworkManager.handleSplitting(packet, NetworkManager.getNetworkChannel()::sendToServer);
    }

    public static void clearStoredPacketsOfUuid(UUID uuid) {
        STORED_PACKET_LIST.row(uuid).clear();
    }

    private static void sendAndStore(ServerPlayerEntity serverPlayer, PacketDistributor.PacketTarget packetDistributor, String packetName, IPacket packet) {
        sendPacket(packetDistributor, packet);
        storePacket(serverPlayer.getUUID(), packetName, packet);
    }

    private static void storePacket(UUID uuid, String packetName, IPacket packet) {
        STORED_PACKET_LIST.put(uuid, packetName, packet);
    }
}
