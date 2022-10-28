package dev.sleep.scorelib.common.network.packet;

import com.google.common.collect.Maps;
import com.google.common.primitives.Bytes;
import dev.sleep.scorelib.SCoreLib;
import dev.sleep.scorelib.common.network.NetworkManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PacketSplit implements IPacket {

    private boolean terminator = false;
    private byte[] payload;

    private int communicationId = -1;
    private int packetIndex = -1;
    private int innerMessageId = -1;

    public PacketSplit() {
    }

    public PacketSplit(final int communicationId, final int packetIndex, final boolean terminator, final int innerMessageId, final byte[] payload) {
        this.communicationId = communicationId;
        this.packetIndex = packetIndex;
        this.terminator = terminator;
        this.innerMessageId = innerMessageId;
        this.payload = payload;
    }

    @Override
    public void toBytes(final PacketBuffer buf) {
        buf.writeVarInt(this.communicationId);
        buf.writeVarInt(this.packetIndex);
        buf.writeBoolean(this.terminator);
        buf.writeVarInt(this.innerMessageId);
        buf.writeByteArray(this.payload);
    }

    @Override
    public void fromBytes(final PacketBuffer buf) {
        this.communicationId = buf.readVarInt();
        this.packetIndex = buf.readVarInt();
        this.terminator = buf.readBoolean();
        this.innerMessageId = buf.readVarInt();
        this.payload = buf.readByteArray();
    }

    @Override
    public void onExecute(final NetworkEvent.Context ctxIn, final boolean isLogicalServer) {
        try {
            //Sync on the message cache since this is still on the Netty thread.
            synchronized (NetworkManager.getMessageCache()) {
                NetworkManager.getMessageCache().get(this.communicationId, Maps::newConcurrentMap).put(this.packetIndex, this.payload);
            }

            if (!this.terminator) {
                //We are not the last message stop executing.
                return;
            }

            //No need to sync again, since we are now the last packet to arrive.
            //All data gets sorted and appended.
            final byte[] packetData = NetworkManager.getMessageCache().get(this.communicationId, Maps::newConcurrentMap).entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(Map.Entry::getValue)
                    .reduce(new byte[0], Bytes::concat);

            //Grab the entry from the inner message id.
            final NetworkManager.NetworkingMessageEntry<?> messageEntry = NetworkManager.getPacketType().get(this.innerMessageId);

            //Create a message.
            final IPacket packet = messageEntry.getCreator().get();

            //Create a new buffer that reads from the packet data and then deserialize the inner message.
            final ByteBuf buffer = Unpooled.wrappedBuffer(packetData);
            packet.fromBytes(new PacketBuffer(buffer));
            buffer.release();

            //Execute the message.
            final LogicalSide packetOrigin = ctxIn.getDirection().getOriginationSide();
            if (packet.getExecutionSide() != null && packetOrigin.equals(packet.getExecutionSide())) {
                SCoreLib.getLogger().warn("Receving {} at wrong side!", packet.getClass().getName());
                return;
            }

            // boolean param MUST equals true if packet arrived at logical server
            ctxIn.enqueueWork(() -> packet.onExecute(ctxIn, packetOrigin.equals(LogicalSide.CLIENT)));
        } catch (ExecutionException e) {
            SCoreLib.getLogger().warn("Failed to handle split packet.", e);
        }
    }
}
