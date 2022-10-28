package dev.sleep.scorelib.common.network;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import dev.sleep.scorelib.SCoreLib;
import dev.sleep.scorelib.common.network.packet.IPacket;
import dev.sleep.scorelib.common.network.packet.PacketSplit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NetworkManager {

    @Getter
    private static final Map<Integer, NetworkingMessageEntry<?>> PacketType = Maps.newHashMap();

    @Getter
    private static final Map<Class<? extends IPacket>, Integer> PacketTypeToID = Maps.newHashMap();

    @Getter
    private static final AtomicInteger messageCounter = new AtomicInteger();

    @Getter
    private static final Cache<Integer, Map<Integer, byte[]>> messageCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .concurrencyLevel(8)
            .build();

    @Getter
    private static SimpleChannel networkChannel;

    public static void initialize(String protocolVersion, String modID, String channelName) {
        NetworkManager.networkChannel = NetworkRegistry.newSimpleChannel(new ResourceLocation(modID, channelName), () -> protocolVersion,
                protocolVersion::equals, protocolVersion::equals);
        registerSplitPacket();
    }

    private static void registerSplitPacket() {
        networkChannel.registerMessage(0, PacketSplit.class, IPacket::toBytes, (buf) -> {
            final PacketSplit msg = new PacketSplit();
            msg.fromBytes(buf);
            return msg;
        }, (msg, ctxIn) -> {
            final NetworkEvent.Context ctx = ctxIn.get();
            final LogicalSide packetOrigin = ctx.getDirection().getOriginationSide();
            ctx.setPacketHandled(true);
            msg.onExecute(ctx, packetOrigin.equals(LogicalSide.CLIENT));
        });
    }

    /**
     * Method that handles the splitting of the message into chunks if need be.
     *
     * @param packet               The message to split in question.
     * @param splitMessageConsumer The consumer that sends away the split parts of the message.
     */
    public static void handleSplitting(final IPacket packet, final Consumer<IPacket> splitMessageConsumer) {
        //Get the inner message id and check if it is known.
        final int messageId = NetworkManager.getPacketTypeToID().getOrDefault(packet.getClass(), -1);
        if (messageId == -1) {
            throw new IllegalArgumentException("The message is unknown to this channel!");
        }

        //Write the message into a buffer and copy that buffer into a byte array for processing.
        final ByteBuf buffer = Unpooled.buffer();
        final PacketBuffer innerPacketBuffer = new PacketBuffer(buffer);
        packet.toBytes(innerPacketBuffer);
        final byte[] data = buffer.array();
        buffer.release();

        //Some tracking variables.
        //Max packet size: 90% of maximum.
        final int max_packet_size = 943718; //This is 90% of max packet size.
        //The current index in the data array.
        int currentIndex = 0;
        //The current index for the split packets.
        int packetIndex = 0;
        //The communication id.
        final int comId = messageCounter.getAndIncrement();

        //Loop while data is available.
        while (currentIndex < data.length) {
            //Tell the network message entry that we are splitting a packet.
            NetworkManager.getPacketType().get(messageId).onSplitting(packetIndex);

            final int extra = Math.min(max_packet_size, data.length - currentIndex);
            //Extract the sub data array.
            final byte[] subPacketData = Arrays.copyOfRange(data, currentIndex, currentIndex + extra);

            //Construct the wrapping packet.
            final PacketSplit splitPacketMessage = new PacketSplit(comId, packetIndex++, (currentIndex + extra) >= data.length, messageId, subPacketData);

            //Send the wrapping packet.
            splitMessageConsumer.accept(splitPacketMessage);

            //Move our working index.
            currentIndex += extra;
        }
    }


    public static final class NetworkingMessageEntry<MSG extends IPacket> {

        private final AtomicBoolean hasWarned = new AtomicBoolean(true);
        private final Supplier<MSG> creator;

        private NetworkingMessageEntry(final Supplier<MSG> creator) {
            this.creator = creator;
        }

        public Supplier<MSG> getCreator() {
            return creator;
        }

        public void onSplitting(int packetIndex) {
            if (packetIndex != 1) {
                return;
            }

            if (!hasWarned.getAndSet(false)) {
                return;
            }

            SCoreLib.getLogger().warn("Splitting message: " + creator.get().getClass());
        }
    }
}
